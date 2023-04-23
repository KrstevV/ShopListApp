package com.example.barkodershopapp

import com.example.barkodershopapp.data.room.ProductDataEntity

interface OnClickListenerButtons {

    fun onClickPlus(list : ProductDataEntity)
    fun onClickMinus(list : ProductDataEntity)
}