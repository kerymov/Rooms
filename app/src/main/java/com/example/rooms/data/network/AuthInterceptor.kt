package com.example.rooms.data.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val token: String?): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}