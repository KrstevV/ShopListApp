package com.example.barkodershopapp.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_table")
data class ListDataEntity (
    @ColumnInfo(name = "list_products")
    var listProducts : ProductDataEntity,
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0L,
        )