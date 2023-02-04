package com.example.part_timedatemanagement

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.part_timedatemanagement.Database.Item
import com.example.part_timedatemanagement.Database.ItemRepository
import com.example.parttimeduedatemanagement.Database.CheckItemList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ItemViewModel(application : Application) : AndroidViewModel(application){
    private val mItemRepository : ItemRepository = ItemRepository.get(application)

    private var itemLiveData = MutableLiveData<List<CheckItemList>>()
    val items : LiveData<List<CheckItemList>> get() = itemLiveData

    var targetItem = Item()

    fun fetchItems(){
        viewModelScope.launch(Dispatchers.IO){
            val news = toListItems(mItemRepository.getAll())
            itemLiveData.postValue(news)
        }
        Log.d("BottomDialog","fetchItems is done")
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
    fun deleteItem(id : Int){
        viewModelScope.launch(Dispatchers.IO){
            mItemRepository.deleteItem(id)
        }
    }
    fun insert(item : Item){
        viewModelScope.launch(Dispatchers.IO){
            mItemRepository.insert(item)
        }
        Log.d("ViewModel","Insert Done")
    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            mItemRepository.deleteAll()
        }
    }
    fun update(id : Int, name : String, location : String, date : String){
        viewModelScope.launch(Dispatchers.IO){
            mItemRepository.update(id, name, location, date)
        }
    }
    suspend fun searchItem(id : Int) : Deferred<Item> =
        viewModelScope.async(Dispatchers.IO){
            return@async mItemRepository.searchItem(id)
        }
}