package com.example.barkodershopapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.barkodershopapp.typeconverters.ProductConverter
import com.example.barkodershopapp.typeconverters.RoomListConverter

@Database(entities = [ListDataEntity::class], version = 5)
@TypeConverters(ProductConverter::class)
abstract class ListDatabase : RoomDatabase() {

    abstract fun listDao() : ListDao

    companion object {

        @Volatile
        var INSTANCE: ListDatabase? = null


        fun getListInstance(context: Context): ListDatabase {
            var tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                var instance = Room.databaseBuilder(
                    context.applicationContext,
                    ListDatabase::class.java,
                    "list_database"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                return instance


            }


        }
    }
}