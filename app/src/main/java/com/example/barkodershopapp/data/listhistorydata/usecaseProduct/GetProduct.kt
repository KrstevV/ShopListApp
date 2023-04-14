package com.example.barkodershopapp.data.listhistorydata.usecaseProduct

import com.example.barkodershopapp.data.listhistorydata.HistoryDataRepository
import com.example.barkodershopapp.data.listproductdata.ProductDataRepository

class GetProduct (private val repository: ProductDataRepository) {

    suspend operator fun invoke(id : Long) = repository.getProductList(id)

}