package com.example.barkoder.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class HistoryDataEntity (

    @ColumnInfo(name = "ListName")
     var listName : String,
    @ColumnInfo(name = "Created_date")
     var listCreated : String,
    @ColumnInfo(name = "total")
    var totalCost : String,
    @ColumnInfo(name = "list_size")
    var listSize : String,
    @PrimaryKey(autoGenerate = true)
      var id : Long = 0L,


    )