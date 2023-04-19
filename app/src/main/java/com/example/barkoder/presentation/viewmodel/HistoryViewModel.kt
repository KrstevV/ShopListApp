package com.example.barkoder.presentation.viewmodel

import androidx.lifecycle.*
import com.example.barkoder.data.listhistorydata.HistoryDataRepository
import com.example.barkoder.data.room.HistoryDataEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val repository : HistoryDataRepository) : ViewModel() {

    val allNotes : LiveData<MutableList<HistoryDataEntity>> = repository.allNotes

    fun insert(list : HistoryDataEntity) = viewModelScope.launch {
        repository.insert(list)
    }
    fun delete() = viewModelScope.launch {
        repository.delete()
    }
    fun deleteItem(list: HistoryDataEntity) = viewModelScope.launch {
        repository.deleteItem(list)
    }
    fun getItem(id: Long) = viewModelScope.launch {
        repository.getItem(id)
    }
    fun updateItem(list: HistoryDataEntity) = viewModelScope.launch {
        repository.updateItem(list)
    }

}