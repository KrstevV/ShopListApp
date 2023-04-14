package com.example.barkodershopapp.data.listhistorydata

interface HistoryDataSource {

    suspend fun addHistoryList(list : HistoryListData)

    suspend fun deleteHistoryList(list : HistoryListData)

    suspend fun getHistoryList(id : Long) : HistoryListData?

    suspend fun getAllHistoryList() : List<HistoryListData>

}