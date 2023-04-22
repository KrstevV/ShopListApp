package com.example.barkoder.di

import android.app.Application
import com.example.barkoder.data.retrofit.ApiProducts
import com.example.barkoder.data.room.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    @Provides
    @Singleton
    fun getProductsA() : ApiProducts =
        Retrofit.Builder()
            .baseUrl("https://a8c7f3b5-21cc-4518-8354-305d18d16f14.mock.pstmn.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiProducts::class.java)

}