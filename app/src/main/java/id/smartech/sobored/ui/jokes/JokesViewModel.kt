package id.smartech.sobored.ui.jokes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.smartech.sobored.remote.ApiService
import id.smartech.sobored.ui.jokes.model.JokesModel
import id.smartech.sobored.ui.suggestion.model.ResultModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class JokesViewModel: ViewModel(), CoroutineScope {
    private lateinit var services: ApiService

    val onSuccessLiveData = MutableLiveData<JokesModel>()
    val onFailLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(services: ApiService) {
        this.services = services
    }

    fun getJokesPlease() {
        launch {
            isLoadingLiveData.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    services.getJokes()
                } catch (e: Throwable) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        isLoadingLiveData.value = false
                        onFailLiveData.value = true
                    }
                }
            }

            if (response is JokesModel) {
                isLoadingLiveData.value = false
                onFailLiveData.value = response.error
                onSuccessLiveData.value = response
            } else {
                isLoadingLiveData.value = false
                onFailLiveData.value = true
            }
        }
    }
}