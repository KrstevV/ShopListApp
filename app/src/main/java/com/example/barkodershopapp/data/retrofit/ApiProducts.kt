package com.example.barkodershopapp.data.retrofit

import com.example.barkodershopapp.domain.userdataacc.ApiData
import retrofit2.Response
import retrofit2.http.GET

interface ApiProducts {

    @GET("product")
    suspend fun getApiProducts() : Response<ApiData>
}