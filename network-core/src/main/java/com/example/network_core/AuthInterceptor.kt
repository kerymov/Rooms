package com.example.network_core

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val token: String?): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}