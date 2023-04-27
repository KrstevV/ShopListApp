package com.example.barkodershopapp.presentation

import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.domain.userdataacc.Product

interface OnClickSelectProduct {

    fun onClickSelect(list : ProductDataEntity)

}