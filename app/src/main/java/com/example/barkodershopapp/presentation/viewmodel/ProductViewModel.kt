package com.example.barkodershopapp.presentation.viewmodel

import androidx.lifecycle.*
import com.example.barkodershopapp.data.listproductdata.ProductDataRepository
import com.example.barkodershopapp.data.room.ProductDataEntity
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel@Inject constructor(private val repository : ProductDataRepository) : ViewModel() {

    val allNotes: LiveData<MutableList<ProductDataEntity>> = repository.allNotes

    val currentProduct = MutableLiveData<MutableList<ProductDataEntity>>()

    fun savedProducts(lista: ProductDataEntity) {
        viewModelScope.launch {
            val list = arrayListOf<ProductDataEntity>()
            list.add(lista)
            currentProduct.value = list
        }
    }

    fun insert(list: ProductDataEntity) = viewModelScope.launch {
        repository.insert(list)
    }

    fun delete() = viewModelScope.launch {
        repository.delete()
    }

    fun deleteItem(list: ProductDataEntity) = viewModelScope.launch {
        repository.deleteItem(list)
    }

    fun getItem(id: Long) = viewModelScope.launch {
        repository.getItem(id)
    }

    fun updateItem(list: ProductDataEntity) = viewModelScope.launch {
        repository.updateItem(list)
    }
}