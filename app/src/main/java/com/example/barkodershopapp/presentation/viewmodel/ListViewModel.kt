package com.example.barkodershopapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barkodershopapp.data.listproductdata.ListDataRepository
import com.example.barkodershopapp.data.room.ListDataEntity
import com.example.barkodershopapp.data.room.ProductDataEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ListViewModel @Inject constructor(private val repository: ListDataRepository) : ViewModel(){

    val allNotes: LiveData<MutableList<ListDataEntity>> = repository.allNotes

    fun getAllProducts(): LiveData<List<ListDataEntity>> {
        return MutableLiveData<List<ListDataEntity>>().apply {
            value = repository.getAllProducts()
        }
    }

    fun insert(list: ListDataEntity) = viewModelScope.launch {
        repository.insert(list)
    }

    fun delete() = viewModelScope.launch {
        repository.delete()
    }

    fun deleteItem(list: ListDataEntity) = viewModelScope.launch {
        repository.deleteItem(list)
    }

    fun getItem(id: Long) = viewModelScope.launch {
        repository.getItem(id)
    }

    fun updateItem(list: ListDataEntity) = viewModelScope.launch {
        repository.updateItem(list)
    }



}