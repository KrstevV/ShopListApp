package com.example.barkoder

import com.example.barkoder.data.room.ProductDataEntity

interface OnClickListenerButtons {

    fun onClickPlus(list : ProductDataEntity)
    fun onClickMinus(list : ProductDataEntity)
}