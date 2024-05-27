package com.example.rooms.data.network

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {

//    private val httpClient = OkHttpClient.Builder()
//        .addInterceptor { chain ->
//            val requestBuilder: Request.Builder = chain.request().newBuilder()
//            requestBuilder
//                .addHeader("Content-Type", "application/json")
//                .addHeader("accept", "*/*")
//            chain.proceed(requestBuilder.build())
//        }
//        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://team-cubing.azurewebsites.net/api")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val roomsApi: RoomsApi by lazy {
        retrofit.create(RoomsApi::class.java)
    }

    val accountApi: AccountApi by lazy {
        retrofit.create(AccountApi::class.java)
    }
}