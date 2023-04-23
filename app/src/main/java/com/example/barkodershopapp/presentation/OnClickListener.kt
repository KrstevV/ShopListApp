package com.example.barkodershopapp.presentation

import com.example.barkodershopapp.data.room.HistoryDataEntity

interface OnClickListener {

    fun onClick(list : HistoryDataEntity)
}