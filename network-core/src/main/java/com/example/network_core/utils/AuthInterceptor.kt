package com.example.network_core.utils

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenProvider: AuthTokenProvider
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val token = runBlocking { tokenProvider.authToken.firstOrNull() }

        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}