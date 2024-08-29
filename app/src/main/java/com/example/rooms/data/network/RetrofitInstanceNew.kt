package com.example.rooms.data.network

import com.example.rooms.data.network.account.AccountApi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstanceNew {

    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    private fun provideHttpClient(authInterceptor: AuthInterceptor) = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val requestBuilder: Request.Builder = chain.request().newBuilder()
            requestBuilder
                .addHeader("Content-Type", "application/json")
                .addHeader("accept", "*/*")
            chain.proceed(requestBuilder.build())
        }
        .addInterceptor(authInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()

    private fun provideRetrofit(token: String?): Retrofit {
        val authInterceptor = AuthInterceptor(token)
        val httpClient = provideHttpClient(authInterceptor)

        return Retrofit.Builder()
            .baseUrl("https://team-cubing.azurewebsites.net/api/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideAccountApi(token: String?) = provideRetrofit(token).create(AccountApi::class.java)
}