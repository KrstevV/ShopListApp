package com.example.barkoder.data.retrofit

import com.example.barkoder.domain.userdataacc.ApiData
import com.example.barkoder.domain.userdataacc.Product
import retrofit2.Response
import retrofit2.http.GET

interface ApiProducts {

    @GET("product")
    suspend fun getApiProducts() : Response<ApiData>
}