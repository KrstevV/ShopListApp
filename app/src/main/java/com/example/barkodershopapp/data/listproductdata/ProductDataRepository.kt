package com.example.barkodershopapp.data.listproductdata

import androidx.lifecycle.LiveData
import com.example.barkodershopapp.data.room.HistoryDao
import com.example.barkodershopapp.data.room.HistoryDataEntity
import com.example.barkodershopapp.data.room.ProductDao
import com.example.barkodershopapp.data.room.ProductDataEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductDataRepository @Inject constructor(private val dao: ProductDao) {


    var allNotes: LiveData<MutableList<ProductDataEntity>> = dao.getAll()
    suspend fun insert(list: ProductDataEntity) {
        withContext(Dispatchers.IO) {
            dao.insert(list)
        }
    }

    suspend fun delete() {
        withContext(Dispatchers.IO) {
            dao.delete()
        }
    }

    suspend fun deleteItem(lsit: ProductDataEntity) {
        withContext(Dispatchers.IO) {
            dao.deleteItem(lsit)
        }
    }

    suspend fun getItem(id: Long) {
        withContext(Dispatchers.IO) {
            dao.getItem(id)
        }
    }

    suspend fun updateItem(list: ProductDataEntity) {
        withContext(Dispatchers.IO) {
            dao.updateItem(list)
        }
    }
}