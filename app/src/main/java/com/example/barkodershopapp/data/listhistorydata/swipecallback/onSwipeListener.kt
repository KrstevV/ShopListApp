package com.example.barkodershopapp.data.listhistorydata.swipecallback

import com.example.barkodershopapp.data.listhistorydata.HistoryListData
import com.example.barkodershopapp.data.room.HistoryDataEntity

interface onSwipeListener {

    fun swipteLeftToDelete(list : HistoryDataEntity)
}