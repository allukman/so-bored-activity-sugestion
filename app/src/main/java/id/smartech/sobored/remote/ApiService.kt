package id.smartech.sobored.remote

import id.smartech.sobored.ui.model.ResultModel
import retrofit2.http.GET

interface ApiService {

    @GET("activity")
    suspend fun getActivity(): ResultModel
}