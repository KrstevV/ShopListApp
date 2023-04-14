package com.example.barkodershopapp.data.listhistorydata.usecaseProduct

import com.example.barkodershopapp.data.listhistorydata.HistoryDataRepository
import com.example.barkodershopapp.data.listhistorydata.HistoryListData
import com.example.barkodershopapp.data.listproductdata.ProductData
import com.example.barkodershopapp.data.listproductdata.ProductDataRepository

class AddProduct(private val repository: ProductDataRepository) {
    suspend operator fun invoke(list : ProductData) = repository.addProductList(list)
}