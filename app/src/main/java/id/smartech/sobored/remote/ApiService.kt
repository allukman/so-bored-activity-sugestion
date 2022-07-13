package id.smartech.sobored.remote

import id.smartech.sobored.ui.cat.model.CatModel
import id.smartech.sobored.ui.dog.model.DogModel
import id.smartech.sobored.ui.jokes.model.JokesModel
import id.smartech.sobored.ui.suggestion.model.ResultModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("activity")
    suspend fun getActivity(): ResultModel

    @GET("joke/Any")
    suspend fun getJokes(): JokesModel

    @GET("woof.json")
    suspend fun getWoof(): DogModel

    @GET("images/search")
    suspend fun getMeow(): Response<List<CatModel>>
}