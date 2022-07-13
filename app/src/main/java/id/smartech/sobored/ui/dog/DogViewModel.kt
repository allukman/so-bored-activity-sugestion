package id.smartech.sobored.ui.dog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.smartech.sobored.remote.ApiService
import id.smartech.sobored.ui.cat.model.CatModel
import id.smartech.sobored.ui.dog.model.DogModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DogViewModel : ViewModel(), CoroutineScope {
    private lateinit var services: ApiService

    val onSuccessLiveData = MutableLiveData<DogModel>()
    val isLoadingLiveData = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(services: ApiService) {
        this.services = services
    }

    fun getDogImage() {
        launch {
            isLoadingLiveData.value = true
            val response = services.getWoof()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    onSuccessLiveData.value = response.body()
                    isLoadingLiveData.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        isLoadingLiveData.value = false
    }
}