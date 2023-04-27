package com.example.barkodershopapp.data.room

import android.graphics.Bitmap
import android.media.Image
import android.os.Parcelable
import android.widget.ImageView
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
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
    var imageProduct : ByteArray?,
    @ColumnInfo(name = "count")
    var count : Int,
    @ColumnInfo(name= "total price")
    var totalPrice : Int,
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0L

        ) : Parcelable
