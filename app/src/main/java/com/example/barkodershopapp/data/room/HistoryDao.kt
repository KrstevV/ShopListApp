package com.example.barkodershopapp.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.barkodershopapp.data.listhistorydata.HistoryListData


@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert (list : HistoryDataEntity)

    @Query("SELECT * FROM history_table")
    fun getAll() : List<HistoryDataEntity>

    @Query("DELETE FROM history_table")
    fun delete()

    @Delete
    fun deleteItem(list: HistoryDataEntity)

    @Query("SELECT * FROM history_table WHERE id = :itemId")
    fun getItem(itemId: Long): HistoryDataEntity

    @Update
    fun updateItem(note: HistoryDataEntity)

}