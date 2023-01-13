package com.example.part_timedatemanagement.Database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1)
abstract class ItemDatabase : RoomDatabase(){
    abstract fun itemDao() : ItemDao

    companion object{
        @Volatile
        private var instance : ItemDatabase? = null

        fun getInstance(application : Application) : ItemDatabase = instance ?: synchronized(this){
            instance ?: buildDatabase(application).also {instance = it}
        }

        private fun buildDatabase(context : Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ItemDatabase::class.java,
                "Item.db"
            ).build()
    }
}