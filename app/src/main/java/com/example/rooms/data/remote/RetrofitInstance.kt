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
    private var token = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJuYW1laWQiOiJrZXJ5bW92IiwibmJmIjoxNzE3OTYwMTIwLCJleHAiOjE3MTg1NjQ5MjAsImlhdCI6MTcxNzk2MDEyMH0.EpInCcWX7Qfka9RnHhYJ14OQWuDKguiGWku-iZPLwExmHiyYz4MtGtMQ5-SxyQmcTvpXaVoh1QGGHhW9IP1W_Q"

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