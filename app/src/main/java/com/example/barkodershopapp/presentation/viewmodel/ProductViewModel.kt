package com.example.barkodershopapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barkodershopapp.data.listhistorydata.HistoryDataRepository
import com.example.barkodershopapp.data.listproductdata.ProductDataRepository
import com.example.barkodershopapp.data.room.HistoryDataEntity
import com.example.barkodershopapp.data.room.ProductDataEntity
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel@Inject constructor(private val repository : ProductDataRepository) : ViewModel() {

    val allNotes: LiveData<MutableList<ProductDataEntity>> = repository.allNotes

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