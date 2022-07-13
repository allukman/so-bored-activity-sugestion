package id.smartech.sobored.remote

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        private const val BASE_URL_ACTIVITIES = "http://www.boredapi.com/api/"
        private const val BASE_URL_JOKES ="https://v2.jokeapi.dev/"
        private const val BASE_URL_DOG ="https://random.dog/"
        private const val BASE_URL_CAT ="https://api.thecatapi.com/v1/"


        private fun provideHttpLoggingInterceptor() = run {
            HttpLoggingInterceptor().apply {
                apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            }
        }

        fun getApiActivities(context: Context): Retrofit {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(provideHttpLoggingInterceptor())
                .addInterceptor(HeaderInterceptor(context))
                .connectTimeout(15, TimeUnit.MINUTES)
                .readTimeout(15, TimeUnit.MINUTES)
                .writeTimeout(15, TimeUnit.MINUTES)
                .callTimeout(30, TimeUnit.MINUTES)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL_ACTIVITIES)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun getApiJokes(context: Context): Retrofit {
            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(provideHttpLoggingInterceptor())
                    .addInterceptor(HeaderInterceptor(context))
                    .connectTimeout(15, TimeUnit.MINUTES)
                    .readTimeout(15, TimeUnit.MINUTES)
                    .writeTimeout(15, TimeUnit.MINUTES)
                    .callTimeout(30, TimeUnit.MINUTES)
                    .build()

            return Retrofit.Builder()
                    .baseUrl(BASE_URL_JOKES)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        fun getApiDog(context: Context): Retrofit {
            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(provideHttpLoggingInterceptor())
                    .addInterceptor(HeaderInterceptor(context))
                    .connectTimeout(15, TimeUnit.MINUTES)
                    .readTimeout(15, TimeUnit.MINUTES)
                    .writeTimeout(15, TimeUnit.MINUTES)
                    .callTimeout(30, TimeUnit.MINUTES)
                    .build()

            return Retrofit.Builder()
                    .baseUrl(BASE_URL_DOG)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        fun getApiCat(context: Context): Retrofit {
            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(provideHttpLoggingInterceptor())
                    .addInterceptor(HeaderInterceptor(context))
                    .connectTimeout(15, TimeUnit.MINUTES)
                    .readTimeout(15, TimeUnit.MINUTES)
                    .writeTimeout(15, TimeUnit.MINUTES)
                    .callTimeout(30, TimeUnit.MINUTES)
                    .build()

            return Retrofit.Builder()
                    .baseUrl(BASE_URL_CAT)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
    }
}