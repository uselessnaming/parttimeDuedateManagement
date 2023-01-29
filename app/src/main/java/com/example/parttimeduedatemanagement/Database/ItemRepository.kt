package com.example.part_timedatemanagement.Database

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.parttimeduedatemanagement.Database.CheckItemList

class ItemRepository (application : Application) {
    private var mItemDatabase : ItemDatabase
    private var mItemDao : ItemDao
    init{
        mItemDatabase = ItemDatabase.getInstance(application)
        mItemDao = mItemDatabase.itemDao()
    }
    fun getAll() : List<Item>{
        return mItemDao.getNew()
    }
    fun searchItem(id : Int) : Item{
        return mItemDao.searchItem(id)
    }

    fun deleteAll(){
        mItemDao.deleteAll()
    }
    fun insert(item : Item){
        mItemDao.insert(item)
    }
    fun delete(item : Item){
        mItemDao.delete(item)
    }
    fun update(item : Item){
        mItemDao.update(item)
    }
    companion object{
        private var INSTANCE : ItemRepository? = null
        /*
        fun initialize(context : Context){
            if(INSTANCE == null){
                INSTANCE = ItemRepository(application)
            }
        }*/

        fun get(appliation : Application) : ItemRepository{
            return INSTANCE ?:
            ItemRepository(appliation)
            //throw IllegalStateException("TodoRepository must be initialized")
        }
    }
}