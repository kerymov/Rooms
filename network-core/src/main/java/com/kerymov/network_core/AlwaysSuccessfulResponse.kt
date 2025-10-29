package com.kerymov.network_core

interface AlwaysSuccessfulResponse {

    val isSuccess: Boolean
    val statusCode: Int
    val errorMessage: String?
}
