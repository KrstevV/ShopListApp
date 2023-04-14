package com.example.barkodershopapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.barkodershopapp.data.listhistorydata.usecaseProduct.*
import com.example.barkodershopapp.data.listproductdata.ProductData
import com.example.barkodershopapp.data.listproductdata.ProductDataRepository
import com.example.barkodershopapp.data.room.ProductRoomSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {
            private val corutineScope = CoroutineScope(Dispatchers.IO)

             val repository = ProductDataRepository(ProductRoomSource(application))

    val useCasesProducts = ProductUseCases(
        AddProduct(repository),
        GetAllProducts(repository),
        DeleteProduct(repository),
        GetProduct(repository)
    )

    var savedProduct = MutableLiveData<Boolean>()
    var listsProducts = MutableLiveData<List<ProductData>>()

    fun savedProducts(list : ProductData) {
        corutineScope.launch {
            useCasesProducts.addProduct(list)
            savedProduct.postValue(true)
        }
    }

    fun getProductsLists() {
        corutineScope.launch {
            val lists : List<ProductData> = useCasesProducts.getAllProducts()
            listsProducts.postValue(lists)
        }
    }
}