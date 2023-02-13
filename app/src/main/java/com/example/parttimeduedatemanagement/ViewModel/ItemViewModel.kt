package com.example.part_timedatemanagement

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.part_timedatemanagement.Database.Item
import com.example.part_timedatemanagement.Database.ItemRepository
import com.example.parttimeduedatemanagement.Database.CheckItemList
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ItemViewModel(application : Application) : AndroidViewModel(application){
    private val TAG = "ViewModel"
    private val mItemRepository : ItemRepository = ItemRepository.get(application)
    suspend fun getType() : Deferred<List<String>> =
        viewModelScope.async(Dispatchers.IO + coroutineException){
            val result = arrayListOf<String>()
            val locations = mItemRepository.getType()
            locations.forEach{
                if (it !in result){
                    result.add(it)
                }
            }
            result.toList()
            return@async result
        }

    private var itemLiveData = MutableLiveData<List<CheckItemList>>()
    val items : LiveData<List<CheckItemList>> get() = itemLiveData

    var count : Int = 0

    fun fetchItems(){
        viewModelScope.launch(Dispatchers.IO){
            count = 0
            val news = toListItems(mItemRepository.getAll())
            itemLiveData.postValue(news)
        }
        Log.d("BottomDialog","fetchItems is done")
    }
    fun getItemCount() : Int { return count }

    private fun toListItems(items : List<Item>) : List<CheckItemList>{
        val result = arrayListOf<CheckItemList>()
        val headers = arrayListOf<String>()

        items.forEach{
            if (it.location !in headers){
                headers.add(it.location)
                result.add(CheckItemList.Header(it))
            }
            if (it.itemName != ""){
                result.add(CheckItemList.Child(it))
                count += 1
            }
            result.sortWith(compareBy { it.item.location})
        }
        return result
    }

    /* DuedateCheckFragment에서 사용할 데이터 가공 */
    private var goneItemLiveData = MutableLiveData<List<Item>>()
    val goneItems : LiveData<List<Item>> get() = goneItemLiveData

    private val coroutineException = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }

    fun goneItemsFetch(today : String){
        viewModelScope.launch(Dispatchers.IO + coroutineException){
            val news = getGoneItems(mItemRepository.getAll(),today)
            goneItemLiveData.postValue(news)
        }
    }
    private fun getGoneItems(items : List<Item>, today : String) : List<Item>{
        val result = arrayListOf<Item>()
        val baseFormat = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
        val currentDate = LocalDate.parse(today,baseFormat)

        items.forEach{
            val targetDate = LocalDate.parse(it.date,baseFormat)
            if (currentDate.isAfter(targetDate)){
                result.add(it)
            }
        }
        Log.d(TAG,"result ? : ${result}")
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
    fun deleteType(type : String, itemName : String){
        viewModelScope.launch(Dispatchers.IO){
            mItemRepository.deleteType(type, itemName)
        }
    }
    suspend fun searchItem(id : Int) : Deferred<Item> =
        viewModelScope.async(Dispatchers.IO){return@async mItemRepository.searchItem(id) }
    suspend fun checkType(type : String) : Deferred<List<String>> =
        viewModelScope.async(Dispatchers.IO + coroutineException){
            Log.d("itemViewModel","${mItemRepository.checkType(type)}")
            return@async mItemRepository.checkType(type)
        }
}