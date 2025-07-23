package com.kerymov.network_core.utils

import com.kerymov.domain_core.auth.AuthTokenProvider
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenProvider: AuthTokenProvider
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val token = runBlocking { tokenProvider.authToken.firstOrNull() }

        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}