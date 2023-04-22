package com.example.barkoder.data.listproductdata

import com.example.barkoder.data.retrofit.ApiProducts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductApiRepository @Inject constructor(var api : ApiProducts){

    suspend fun getProductsAPI() = withContext(Dispatchers.IO) {
        api.getApiProducts()
    }
}