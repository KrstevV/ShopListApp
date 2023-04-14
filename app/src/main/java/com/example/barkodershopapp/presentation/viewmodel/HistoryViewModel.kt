package com.example.barkodershopapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.barkodershopapp.data.listhistorydata.HistoryDataRepository
import com.example.barkodershopapp.data.listhistorydata.HistoryListData
import com.example.barkodershopapp.data.listhistorydata.usecaseHistory.*
import com.example.barkodershopapp.data.room.HistoryRoomSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel (aplication : Application) : AndroidViewModel(aplication) {
            private val corutineScope = CoroutineScope(Dispatchers.IO)

            val repository = HistoryDataRepository(HistoryRoomSource(aplication))

            val useCasesHistory = HistoryUseCases(
                AddHistoryList(repository),
                GetAllHistoryList(repository),
                DeleteHistoryData(repository),
                GetHistoryList(repository)
            )

        var savedHistory = MutableLiveData<Boolean>()
        var listsHistory = MutableLiveData<List<HistoryListData>>()


        fun savedHistory(list : HistoryListData) {
            corutineScope.launch {
                useCasesHistory.addHistory(list)
                savedHistory.postValue(true)
            }
        }

        fun getHistoryLists() {
            corutineScope.launch {
                val lists : List<HistoryListData> = useCasesHistory.getAllHistory()
                listsHistory.postValue(lists)
            }
        }

}