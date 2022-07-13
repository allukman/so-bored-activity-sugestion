    package id.smartech.sobored.ui.cat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.smartech.sobored.remote.ApiService
import id.smartech.sobored.ui.cat.model.CatModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CatViewModel: ViewModel(), CoroutineScope {
    private lateinit var services: ApiService

    val onSuccessLiveData = MutableLiveData<CatModel>()
    val onFailLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(services: ApiService) {
        this.services = services
    }

    fun getCatImage() {
        launch {
            isLoadingLiveData.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    services.getMeow()
                } catch (e: Throwable) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        isLoadingLiveData.value = false
                        onFailLiveData.value = true
                    }
                }
            }

            if (response is CatModel) {
                isLoadingLiveData.value = false
                onFailLiveData.value = false
                onSuccessLiveData.value = response
                Log.d("response", "success")
            } else {
                isLoadingLiveData.value = false
                onFailLiveData.value = true
            }
        }
    }
}