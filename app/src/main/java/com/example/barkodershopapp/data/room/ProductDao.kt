package com.example.barkodershopapp.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.barkodershopapp.data.listhistorydata.HistoryListData


@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert (list : ProductDataEntity)

    @Query("SELECT * FROM product_table")
    fun getAll() :  List<ProductDataEntity>

    @Query("DELETE FROM product_table")
    fun delete ()

    @Delete
    fun deleteItem(list: ProductDataEntity)

    @Query("SELECT * FROM product_table WHERE id = :itemId")
    fun getItem(itemId: Long): ProductDataEntity

    @Update
    fun updateItem(note: ProductDataEntity)
}