package com.example.barkoder.di

import android.app.Application
import com.example.barkoder.data.room.HistoryDao
import com.example.barkoder.data.room.HistoryDatabase
import com.example.barkoder.data.room.ProductDao
import com.example.barkoder.data.room.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object HistoryModule {

    @Singleton
    @Provides
    fun getHistoryDB(context : Application) : HistoryDatabase {
        return HistoryDatabase.getHistoryInstance(context)
    }

    @Singleton
    @Provides
    fun getHistoryDao(historyDB : HistoryDatabase) : HistoryDao {
        return historyDB.historyDao()
    }

    @Singleton
    @Provides
    fun getProductDB(context : Application) : ProductDatabase {
        return ProductDatabase.getProductInstance(context)
    }

    @Singleton
    @Provides
    fun getProductDao(productDB : ProductDatabase) : ProductDao {
        return productDB.productDao()
    }


}