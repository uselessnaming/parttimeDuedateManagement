package com.example.part_timedatemanagement.Database

import android.app.Application
import com.example.parttimeduedatemanagement.Database.Item

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
    fun deleteType(type : String, itemName : String){
        mItemDao.deleteType(type,itemName)
    }
    fun searchItem(id : Int) : Item {
        return mItemDao.searchItem(id)
    }
    fun getType() : List<String>{
        return mItemDao.getType()
    }
    fun checkType(type : String) : List<String>{
        return mItemDao.checkType(type)
    }
    fun updateEA(id : Int, ea : Int) {
        return mItemDao.updateEA(id, ea)
    }
    fun updateIsEmpty(id : Int, isEmpty : Boolean){
        val isEmptyToInt = if (isEmpty) 1 else 0
        mItemDao.updateIsEmpty(id, isEmptyToInt)
    }
    fun checkItem(itemName : String, location : String) : Boolean {
        return mItemDao.checkItem(itemName,location)
    }
    fun resetEA(){
        mItemDao.resetEA()
    }
    companion object{
        private var INSTANCE : ItemRepository? = null

        fun get(application : Application) : ItemRepository{
            return INSTANCE ?:
            ItemRepository(application)
            //throw IllegalStateException("TodoRepository must be initialized")
        }
    }
}