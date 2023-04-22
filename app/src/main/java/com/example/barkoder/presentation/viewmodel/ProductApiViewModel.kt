package com.example.barkoder.presentation.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barkoder.data.listproductdata.ProductApiRepository
import com.example.barkoder.domain.userdataacc.ApiData
import com.example.barkoder.domain.userdataacc.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductApiViewModel @Inject constructor(var repository : ProductApiRepository) : ViewModel(){

    val resp = MutableLiveData<ApiData>()

    fun getAllProductsApi() = viewModelScope.launch {
        repository.getProductsAPI().let { response ->
            if(response.isSuccessful) {
                resp.value = response.body()
            } else {
                println("somehing hoes wrong")
            }
        }
    }

}