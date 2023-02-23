package com.example.part_timedatemanagement.Database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Item::class], version = 3)
abstract class ItemDatabase : RoomDatabase(){
    abstract fun itemDao() : ItemDao

    companion object{
        @Volatile
        private var instance : ItemDatabase? = null

        fun getInstance(application : Application) : ItemDatabase = instance ?: synchronized(this){
            instance ?: buildDatabase(application).also {instance = it}
        }
        private fun buildDatabase(context : Context) : ItemDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ItemDatabase::class.java,
                "Item.db")
                .addMigrations(MIGRATION_1_2,MIGRATION_2_3)
                .build()
        }

        /* version1 -> 2 항목의 개수를 추가 */
        private val MIGRATION_1_2 = object : Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""CREATE TABLE new_Item (id INTEGER PRIMARY KEY NOT NULL, location STRING NOT NULL, itemName STRING NOT NULL, date STRING NOT NULL, ea INTEGER NOT NULL DEFAULT 0)""".trimMargin())
                database.execSQL("""INSERT INTO new_Item (id,location,itemName,date) SELECT id,location,itemName,date FROM Item""".trimIndent())
                database.execSQL("DROP TABLE itemTable")
                database.execSQL("ALTER TABLE new_Item RENAME TO Item")
            }
        }

        /* version2 -> 3 품절되었는지 아닌지를 체크하는 변수 추가 */
        private val MIGRATION_2_3 = object : Migration(2,3){
            override fun migrate(database : SupportSQLiteDatabase){
                database.execSQL("""CREATE TABLE new_Item (id INTEGER PRIMARY KEY NOT NULL, location STRING NOT NULL, itemName STRING NOT NULL, date STRING NOT NULL, ea INTEGER NOT NULL DEFAULT 0, isEmpty INTEGER NOT NULL DEFAULT 0)""".trimMargin())
                database.execSQL("""INSERT INTO new_Item (id,location,itemName,date,ea) SELECT id, location, itemName, date, ea FROM Item""".trimIndent())
                database.execSQL("DROP TABLE itemTable")
                database.execSQL("ALTER TABLE newItem RENAME TO Item")
            }
        }
    }
}