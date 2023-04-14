package com.example.barkodershopapp.data.listproductdata

import com.example.barkodershopapp.data.listhistorydata.HistoryListData

interface ProductDataSource {

    suspend fun addProductList(list : ProductData)

    suspend fun deleteProductList(list : ProductData)

    suspend fun getProductList(id : Long) : ProductData?

    suspend fun getAllProductList() : List<ProductData>
}