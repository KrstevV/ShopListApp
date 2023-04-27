package com.example.barkodershopapp.typeconverters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream


object TypeConverterss {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray) : Bitmap {
      return  BitmapFactory.decodeByteArray(byteArray, 0 , byteArray.size)
    }

//    @TypeConverter
//    fun fromBitmap(bitmap: Bitmap?): ByteArray? {
//        return bitmap?.let { ByteArrayOutputStream().apply { it.compress(Bitmap.CompressFormat.PNG, 100, this) }.toByteArray() }
//    }

}