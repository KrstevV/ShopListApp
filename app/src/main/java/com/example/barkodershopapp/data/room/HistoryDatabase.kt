package com.example.barkodershopapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [HistoryDataEntity::class], version = 1)
abstract class HistoryDatabase : RoomDatabase() {

    abstract fun historyDao() : HistoryDao

    companion object {

        @Volatile
        var INSTANCE: HistoryDatabase? = null


        fun getHistoryInstance(context: Context): HistoryDatabase {
            var tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                var instance = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryDatabase::class.java,
                    "history_database"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                return instance


            }


        }
    }

}