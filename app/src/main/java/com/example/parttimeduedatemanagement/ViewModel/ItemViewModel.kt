package com.example.part_timedatemanagement

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.lifecycle.*
import com.example.part_timedatemanagement.Database.Item
import com.example.part_timedatemanagement.Database.ItemRepository
import com.example.parttimeduedatemanagement.Database.CheckItemList
import com.example.parttimeduedatemanagement.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemViewModel(application : Application) : AndroidViewModel(application){
    private val mItemRepository : ItemRepository = ItemRepository.get(application)

    private var targetItem : Item = Item()
    private var itemLiveData = MutableLiveData<List<CheckItemList>>()
    val items : LiveData<List<CheckItemList>> get() = itemLiveData

    fun fetchItems(){
        viewModelScope.launch(Dispatchers.IO){
            val news = toListItems(mItemRepository.getAll())
            itemLiveData.postValue(news)
        }
    }
    private fun toListItems(items : List<Item>) : List<CheckItemList>{
        val result = arrayListOf<CheckItemList>()

        var groupHeaderLocation = ""
        items.forEach{
            if (groupHeaderLocation != it.location){
                result.add(CheckItemList.Header(it))
            }
            result.add(CheckItemList.Child(it))
            groupHeaderLocation = it.location
        }
        return result
    }
    fun searchItem(id : Int) : Item{
        viewModelScope.launch(Dispatchers.IO){
            targetItem = mItemRepository.searchItem(id)
        }
        return targetItem
    }
    fun insert(item : Item){
        viewModelScope.launch(Dispatchers.IO){
            mItemRepository.insert(item)
        }
        Log.d("ViewModel","Insert Done")
    }

    fun delete(item : Item){
        viewModelScope.launch(Dispatchers.IO){
            mItemRepository.delete(item)
        }
    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            mItemRepository.deleteAll()
        }
    }
    fun update(item : Item){
        viewModelScope.launch(Dispatchers.IO){
            mItemRepository.update(item)
        }
    }
}