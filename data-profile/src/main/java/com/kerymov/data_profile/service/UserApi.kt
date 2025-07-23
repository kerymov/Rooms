package com.kerymov.data_profile.service

import com.kerymov.rooms.data.model.account.results.UserResults
import retrofit2.Response
import retrofit2.http.GET

interface UserApi {

    @GET("Account/results")
    suspend fun getResults(): Response<UserResults>
}