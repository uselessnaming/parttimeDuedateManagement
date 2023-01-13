package com.example.part_timedatemanagement

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.part_timedatemanagement.Database.Item
import com.example.part_timedatemanagement.Database.ItemRepository

class ItemViewModel(application : Application) : AndroidViewModel(application){
    private val mItemRepository : ItemRepository = ItemRepository.get(application)
    private val allItems : LiveData<List<Item>> = mItemRepository.getAll()
    private val allLocations : LiveData<List<String>> = mItemRepository.getLocation()

    fun getLocation() : LiveData<List<String>>{
        return allLocations
    }

    fun getAll() : LiveData<List<Item>>{
        return allItems
    }

    fun searchContent(location : String) : LiveData<List<Item>>{
        return mItemRepository.searchContent(location)
    }

    fun insert(item : Item){
        mItemRepository.insert(item)
    }

    fun delete(item : Item){
        mItemRepository.delete(item)
    }

    fun update(item : Item){
        mItemRepository.update(item)
    }
}