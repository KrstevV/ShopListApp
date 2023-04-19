package com.example.barkoder.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class HistoryDataEntity (

    @ColumnInfo(name = "ListName")
     var listName : String,
    @ColumnInfo(name = "Created date")
     var listCreated : String,
    @PrimaryKey(autoGenerate = true)
      var id : Long = 0L,


    )