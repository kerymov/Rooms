package com.example.rooms.data.network

import com.example.rooms.data.network.scramble.ScrambleApi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration

object RetrofitInstance {

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
        .connectTimeout(Duration.ofSeconds(10))
        .callTimeout(Duration.ofSeconds(10))
        .readTimeout(Duration.ofSeconds(10))
        .writeTimeout(Duration.ofSeconds(10))
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

    fun provideScrambleApi(token: String?): ScrambleApi =
        provideRetrofit(token).create(ScrambleApi::class.java)
}