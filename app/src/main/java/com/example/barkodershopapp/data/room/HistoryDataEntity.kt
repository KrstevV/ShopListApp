package com.example.barkodershopapp.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.barkodershopapp.data.listhistorydata.HistoryListData

@Entity(tableName = "history_table")
data class HistoryDataEntity (

    @ColumnInfo(name = "ListName")
     var listName : String,
    @ColumnInfo(name = "Created date")
     var listCreated : String,
    @PrimaryKey(autoGenerate = true)
var id : Long = 0L,

        ) {
            companion object{
                fun fromHistory(list : HistoryListData) = HistoryDataEntity(list.nameList, list.createdListDate)

            }

            fun toHistory() = HistoryListData(listName, listCreated)

}