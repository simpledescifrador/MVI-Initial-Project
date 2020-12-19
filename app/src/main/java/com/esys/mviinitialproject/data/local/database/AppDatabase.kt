package com.esys.mviinitialproject.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.esys.mviinitialproject.data.model.SampleModel


@Database(entities = [SampleModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    //TODO: ADD DAO HERE
    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "data.db"
                    )
                        .fallbackToDestructiveMigration     ()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}