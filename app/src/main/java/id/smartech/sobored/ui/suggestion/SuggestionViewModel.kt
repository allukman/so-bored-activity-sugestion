package id.smartech.sobored.ui.suggestion

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.smartech.sobored.remote.ApiService
import id.smartech.sobored.ui.suggestion.model.ResultModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SuggestionViewModel: ViewModel(), CoroutineScope {
    private lateinit var services: ApiService

    val onSuccessLiveData = MutableLiveData<ResultModel>()
    val onFailLiveData = MutableLiveData<String>()
    val isLoadingLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
    get() = Job() + Dispatchers.Main

    fun setService(services: ApiService) {
        this.services = services
    }

    fun getActivityResult() {
        launch {
            isLoadingLiveData.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    services.getActivity()
                } catch (e: Throwable) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main){
                        isLoadingLiveData.value = false
                    }
                }
            }

            if (response is ResultModel) {
                isLoadingLiveData.value = false

                onSuccessLiveData.value = response
            } else {
                isLoadingLiveData.value = false
                onFailLiveData.value = "Get Data Failed"
            }
        }
    }


}