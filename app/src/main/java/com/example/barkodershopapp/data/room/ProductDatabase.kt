package com.example.barkodershopapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ProductDataEntity::class], version = 11)
abstract class ProductDatabase : RoomDatabase(){

    abstract fun productDao() : ProductDao


    companion object {

        @Volatile
        var INSTANCE: ProductDatabase? = null


        fun getProductInstance(context: Context): ProductDatabase {
            var tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                var instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductDatabase::class.java,
                    "product_database"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                return instance


            }


        }
    }
}