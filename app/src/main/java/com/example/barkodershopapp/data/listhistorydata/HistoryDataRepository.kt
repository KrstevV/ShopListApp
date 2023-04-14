package com.example.barkodershopapp.data.listhistorydata

class HistoryDataRepository (private val source: HistoryDataSource) {

    suspend fun addHistoryList(list : HistoryListData) = source.addHistoryList(list)

    suspend fun getHistoryList(id : Long) = source.getHistoryList(id)

    suspend fun getAllHistoryList() = source.getAllHistoryList()

    suspend fun deleteHistoryList(list : HistoryListData) = source.deleteHistoryList(list)
}