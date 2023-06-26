package com.example.myapplicationtest2mvi.model.apiServices

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {

    val apis : Apis?
        get() = provideRetrofit()?.create(Apis::class.java)

    companion object {
        private var instance : RetrofitClient? = null
        fun getClient() : RetrofitClient {
            if(instance == null)
                instance = RetrofitClient()
            return instance!!
        }
    }

    private fun provideRetrofit() :Retrofit {
        return Retrofit.Builder()
            .baseUrl(Apis.BASE_URL)
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun provideOkHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30 , TimeUnit.SECONDS)
            .readTimeout(30 , TimeUnit.SECONDS)
            .writeTimeout(30 , TimeUnit.SECONDS)
            .build()
    }

}