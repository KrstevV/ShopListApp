package com.example.barkodershopapp.data.listhistorydata.usecaseHistory

import com.example.barkodershopapp.data.listhistorydata.HistoryDataRepository

class GetHistoryList (private val repository: HistoryDataRepository) {

    suspend operator fun invoke(id : Long) = repository.getHistoryList(id)

}