package com.example.barkoder.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class ProductDataEntity (

    @ColumnInfo(name = "Name Product")
    var nameProduct : String?,
    @ColumnInfo(name = "Barcode")
    var barcodeProduct : String?,
    @ColumnInfo(name ="Notes")
    var noteProduct : String?,
    @ColumnInfo(name = "Price")
    var priceProduct : Int,
    @ColumnInfo(name = "Active")
    var activeProduct : Boolean,
    @ColumnInfo(name = "image")
    var imageProduct : String?,
    @ColumnInfo(name = "count")
    var count : Int,
    @ColumnInfo(name= "total price")
    var totalPrice : Int,
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0L

        )
