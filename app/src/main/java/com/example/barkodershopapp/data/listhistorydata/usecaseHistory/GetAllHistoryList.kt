package com.example.barkodershopapp.data.listhistorydata.usecaseHistory

import com.example.barkodershopapp.data.listhistorydata.HistoryDataRepository

class GetAllHistoryList (val repository: HistoryDataRepository) {

    suspend operator fun invoke() = repository.getAllHistoryList()

}