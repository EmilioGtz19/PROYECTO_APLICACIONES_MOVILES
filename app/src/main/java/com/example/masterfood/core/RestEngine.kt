package com.example.masterfood.core

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestEngine {

    companion object{

        fun getRestEngine(): Retrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            return Retrofit.Builder()
                .baseUrl("https://masterfoodapi.000webhostapp.com/v1/")
                //.baseUrl("http://192.168.1.80:84/masterfood-api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

    }

}