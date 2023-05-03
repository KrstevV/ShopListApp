package com.example.barkodershopapp

import com.example.barkodershopapp.data.room.ListDataEntity
import com.example.barkodershopapp.data.room.ProductDataEntity

interface OnClickListenerButtons {

    fun onClickPlus(list : ListDataEntity)
    fun onClickMinus(list : ListDataEntity)
}