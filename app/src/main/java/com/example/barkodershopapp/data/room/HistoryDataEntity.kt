package com.example.barkodershopapp.data.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
@Entity(tableName = "history_table")
data class HistoryDataEntity (

    @ColumnInfo(name = "ListName")
     var listName : String,
    @ColumnInfo(name = "date")
    var listDate : String,
    @ColumnInfo(name = "List_Products")
    var listProducts : @RawValue ArrayList<ListDataEntity>,
    @PrimaryKey(autoGenerate = true)
      var id : Long = 0L,

    ) : Parcelable