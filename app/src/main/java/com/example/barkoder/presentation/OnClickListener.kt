package com.example.barkoder.presentation

import com.example.barkoder.data.listhistorydata.HistoryListData
import com.example.barkoder.data.room.HistoryDataEntity

interface OnClickListener {

    fun onClick(list : HistoryDataEntity)
}