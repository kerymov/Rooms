package com.example.rooms.data.remote

import android.content.Context
import com.example.rooms.data.remote.rooms.RoomsApi
import com.example.rooms.data.remote.account.AccountApi
import com.example.rooms.data.remote.scramble.ScrambleApi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


object RetrofitInstance {

//    private val sharedPreferences = applicationContext.getSharedPreferences("preference_key", Context.MODE_PRIVATE);
    private var token = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJuYW1laWQiOiJrZXJ5bW92IiwibmJmIjoxNzE3NTE2OTYyLCJleHAiOjE3MTgxMjE3NjIsImlhdCI6MTcxNzUxNjk2Mn0.BFHSau3t_PMdLK7a-CHe2-_8kE1K0DQdBOTXr-3wC35JpWMxUwayPrG9_NLCEdruEp-FVQsCGq64rdTXpxnjvw"

    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val requestBuilder: Request.Builder = chain.request().newBuilder()
            requestBuilder
                .addHeader("Content-Type", "application/json")
                .addHeader("accept", "*/*")
                .addHeader("Authorization", "Bearer $token")
            chain.proceed(requestBuilder.build())
        }
        .addInterceptor(httpLoggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://team-cubing.azurewebsites.net/api/")
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val roomsApi: RoomsApi by lazy {
        retrofit.create(RoomsApi::class.java)
    }

    val accountApi: AccountApi by lazy {
        retrofit.create(AccountApi::class.java)
    }

    val scrambleApi: ScrambleApi by lazy {
        retrofit.create(ScrambleApi::class.java)
    }
}