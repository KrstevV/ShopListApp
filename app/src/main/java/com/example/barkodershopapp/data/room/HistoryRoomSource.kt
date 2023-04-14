package com.example.barkodershopapp.data.room

import android.content.Context
import com.example.barkodershopapp.data.listhistorydata.HistoryDataSource
import com.example.barkodershopapp.data.listhistorydata.HistoryListData

class HistoryRoomSource (context: Context) : HistoryDataSource {

    var historyDaoo = HistoryDatabase.getHistoryInstance(context).historyDao()

    override suspend fun addHistoryList(list: HistoryListData) = historyDaoo.insert(HistoryDataEntity.fromHistory(list))

    override suspend fun deleteHistoryList(list: HistoryListData) = historyDaoo.deleteItem(HistoryDataEntity.fromHistory(list))

    override suspend fun getHistoryList(id: Long): HistoryListData? = historyDaoo.getItem(id)?.toHistory()

    override suspend fun getAllHistoryList() = historyDaoo.getAll().map { it.toHistory() }
}