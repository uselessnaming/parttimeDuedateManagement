package com.example.part_timedatemanagement.Database

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room

class ItemRepository (application : Application) {
    private var mItemDatabase : ItemDatabase
    private var mItemDao : ItemDao
    private var mAllItems : LiveData<List<Item>>
    private var mItemLocation : LiveData<List<String>>

    init{
        mItemDatabase = ItemDatabase.getInstance(application)
        mItemDao = mItemDatabase.itemDao()
        mAllItems = mItemDao.getAll()
        mItemLocation = mItemDao.getLocation()
    }
    fun getLocation() : LiveData<List<String>>{
        return mItemLocation
    }
    fun getAll() : LiveData<List<Item>>{
        return mAllItems
    }
    fun searchContent(location : String) : LiveData<List<Item>>{
        lateinit var mSearchItem : LiveData<List<Item>>
        Thread{
            mSearchItem = mItemDao.searchContent(location)
        }.start()
        return mSearchItem
    }
    fun insert(item : Item){
        Thread{
            mItemDao.insert(item)
        }.start()
    }
    fun delete(item : Item){
        Thread{
            mItemDao.delete(item)
        }.start()
    }
    fun update(item : Item){
        Thread{
            mItemDao.update(item)
        }.start()
    }

    companion object{
        private var INSTANCE : ItemRepository? = null
/*
        fun initialize(context : Context){
            if(INSTANCE == null){
                INSTANCE = ItemRepository(context)
            }
        }
 */
        fun get(appliation : Application) : ItemRepository{
            return INSTANCE ?:
            ItemRepository(appliation)
            //throw IllegalStateException("TodoRepository must be initialized")
        }
    }
}