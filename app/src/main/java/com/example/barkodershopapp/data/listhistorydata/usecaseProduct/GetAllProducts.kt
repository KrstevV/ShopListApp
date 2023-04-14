package com.example.barkodershopapp.data.listhistorydata.usecaseProduct

import com.example.barkodershopapp.data.listhistorydata.HistoryDataRepository
import com.example.barkodershopapp.data.listhistorydata.HistoryListData
import com.example.barkodershopapp.data.listproductdata.ProductDataRepository

class GetAllProducts (private val repository: ProductDataRepository) {
    suspend operator fun invoke() = repository.getAllProductList()
}