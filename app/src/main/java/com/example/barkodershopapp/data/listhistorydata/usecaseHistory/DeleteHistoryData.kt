package com.example.barkodershopapp.data.listhistorydata.usecaseHistory

import com.example.barkodershopapp.data.listhistorydata.HistoryDataRepository
import com.example.barkodershopapp.data.listhistorydata.HistoryListData

class DeleteHistoryData (private val repository: HistoryDataRepository) {

    suspend operator fun invoke(list : HistoryListData) = repository.deleteHistoryList(list)

}