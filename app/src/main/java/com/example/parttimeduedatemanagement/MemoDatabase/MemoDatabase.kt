package com.example.parttimeduedatemanagement.MemoDatabase

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Memo::class], version = 1)
abstract class MemoDatabase : RoomDatabase() {
    abstract fun memoDao() : MemoDao

    companion object{
        @Volatile
        private var instance : MemoDatabase? = null

        fun getInstance(application : Application) : MemoDatabase = instance ?: synchronized(this){
            instance ?: buildDatabase(application).also {instance = it}
        }

        private fun buildDatabase(application : Application) =
            Room.databaseBuilder(
                application.applicationContext,
                MemoDatabase::class.java,
                "Memo.db"
            ).build()
    }
}