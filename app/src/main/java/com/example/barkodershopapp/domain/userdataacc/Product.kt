package com.example.barkodershopapp.domain.userdataacc

data class Product(
    val barcode: String,
    val count: Int,
    val id: String,
    val image: String,
    val name: String,
    val notes: String,
    val price: Int,
    val totalprice: Int
)