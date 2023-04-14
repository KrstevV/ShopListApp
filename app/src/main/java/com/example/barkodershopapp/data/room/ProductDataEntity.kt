package com.example.barkodershopapp.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.barkodershopapp.data.listhistorydata.HistoryListData
import com.example.barkodershopapp.data.listproductdata.ProductData

@Entity(tableName = "product_table")
data class ProductDataEntity (

    @ColumnInfo(name = "Name Product")
    var nameProduct : String,
    @ColumnInfo(name = "Barcode")
    var barcodeProduct : String,
    @ColumnInfo(name ="Notes")
    var noteProduct : String,
    @ColumnInfo(name = "Price")
    var priceProduct : String,
    @ColumnInfo(name = "Active")
    var activeProduct : Boolean,
    @ColumnInfo(name = "image")
    var imageProduct : String,
    @ColumnInfo(name = "count")
    var count : String,
    @PrimaryKey
    var id : Long = 0L

        ) {
    companion object{
        fun fromProduct(list : ProductData) = ProductDataEntity(list.nameProduct,
            list.barcodeProduct,list.noteProduct,list.priceProduct,list.activeProduct,list.imageProduct,list.count)

    }

    fun toProduct() = ProductData(nameProduct, barcodeProduct, noteProduct,priceProduct,activeProduct,imageProduct,count )
}