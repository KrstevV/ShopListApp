package com.example.barkodershopapp.data.listproductdata

import com.example.barkodershopapp.data.listhistorydata.HistoryListData

class ProductDataRepository (private val source : ProductDataSource){

    suspend fun addProductList(list : ProductData) = source.addProductList(list)

    suspend fun getProductList(id : Long) = source.getProductList(id)

    suspend fun getAllProductList() = source.getAllProductList()

    suspend fun deleteProductList(list : ProductData) = source.deleteProductList(list)
}