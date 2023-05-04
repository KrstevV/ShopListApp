package com.example.barkodershopapp.data.db.productdatabase

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.media.Image
import android.os.Parcelable
import android.widget.ImageView
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.barkoder.shoppingApp.net.R
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
    @ColumnInfo(name = "Unit")
    var unitProduct : String?,
    @ColumnInfo(name = "Quantity")
    var quantityProduct : Int,
    @ColumnInfo(name = "Checkout")
    var checkout : Boolean = false,
    @ColumnInfo(name = "image")
    var imageProduct : ByteArray?,
    @ColumnInfo(name = "count")
    var count : Int,
    @ColumnInfo(name= "total price")
    var totalPrice : Int,
    @ColumnInfo(name= "price_history")
    var priceHistory : ArrayList<String>,
    @ColumnInfo(name = "defult_count")
    var defultCount : Int,
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0L

        ) : Parcelable

