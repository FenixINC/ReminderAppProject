package com.example.taras.reminerapp.db.service

import com.example.taras.reminerapp.db.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Taras Koloshmatin on 25.07.2018
 */
class ServiceGenerator {

    companion object SERVICE {
        private val httpClient = OkHttpClient.Builder()

        private val builder: Retrofit.Builder = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())

        fun <S> createService(serviceClass: Class<S>): S {
            val client = httpClient
                    .build()
            val retrofit = builder
                    .client(client)
                    .build()
            return retrofit.create(serviceClass)
        }
    }
}