package com.example.network_core

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
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

    @Provides
    @Singleton
    private fun provideRetrofit(token: String?): Retrofit {
        val authInterceptor = AuthInterceptor(token)
        val httpClient = provideHttpClient(authInterceptor)

        return Retrofit.Builder()
            .baseUrl("https://team-cubing.azurewebsites.net/api/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}