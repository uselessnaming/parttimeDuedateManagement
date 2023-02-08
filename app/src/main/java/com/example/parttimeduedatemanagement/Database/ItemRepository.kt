package com.example.part_timedatemanagement.Database

import android.app.Application

class ItemRepository (application : Application) {
    private var mItemDatabase : ItemDatabase
    private var mItemDao : ItemDao
    init{
        mItemDatabase = ItemDatabase.getInstance(application)
        mItemDao = mItemDatabase.itemDao()
    }
    fun getAll() : List<Item>{
        return mItemDao.getAll()
    }
    fun deleteItem(id : Int){
        mItemDao.deleteItem(id)
    }
    fun deleteAll(){
        mItemDao.deleteAll()
    }
    fun insert(item : Item){
        mItemDao.insert(item)
    }
    fun update(id : Int, name : String, location : String, date : String){
        mItemDao.update(id,name,location,date)
    }
    fun searchItem(id : Int) : Item{
        return mItemDao.searchItem(id)
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