package com.example.parttimeduedatemanagement.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.part_timedatemanagement.Database.Item
import com.example.part_timedatemanagement.Database.ItemRepository
import com.example.parttimeduedatemanagement.Database.CheckItemList
import com.example.parttimeduedatemanagement.Database.EaItem
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ItemViewModel(application : Application) : AndroidViewModel(application){
    private val TAG = "ViewModel"
    private val mItemRepository : ItemRepository = ItemRepository.get(application)

    private var typeLiveData = MutableLiveData<List<String>>()
    val types : LiveData<List<String>> get() = typeLiveData

    private var eaLiveData = MutableLiveData<List<EaItem>>()
    val eaData : LiveData<List<EaItem>>get() = eaLiveData
    val sb = StringBuilder()
    private var curLocation : String = ""

    private var itemLiveData = MutableLiveData<List<CheckItemList>>()
    val items : LiveData<List<CheckItemList>> get() = itemLiveData
    var count : Int = 0

    /* EA 관련 데이터 */
    fun updateEA(id : Int, ea : Int){
        viewModelScope.launch(Dispatchers.IO + coroutineException){
            mItemRepository.updateEA(id, ea)
        }
    }
    fun fetchEas(){
        viewModelScope.launch(Dispatchers.IO + coroutineException){
            val news = toEaList(mItemRepository.getAll())
            eaLiveData.postValue(news)
        }
    }
    private fun toEaList(items : List<Item>) : List<EaItem>{
        val result = arrayListOf<EaItem>()
        val headers = arrayListOf<String>()

        items.forEach{
            if (it.location !in headers){
                headers.add(it.location)
                result.add(EaItem.Header(it))
            }
            if (it.itemName != ""){
                result.add(EaItem.Child(it))
            }
        }
        result.sortWith(compareBy({it.item.location},{it.order}))
        sb.clear()
        curLocation = ""
        result.forEach{
            if (it is EaItem.Child){
                if (it.item.ea != 0){
                    if (curLocation != it.item.location){
                        curLocation = it.item.location
                        sb.append("\n${it.item.location} : ")
                    }
                sb.append("${it.item.itemName}${it.item.ea}\t")
                }
            }
        }
        return result
    }
    fun resetEA(){
        viewModelScope.launch(Dispatchers.IO){
            mItemRepository.resetEA()
        }
    }
    /* type을 받는 live data */
    fun fetchTypes() {
        viewModelScope.launch(Dispatchers.IO){
            val news = getType()
            typeLiveData.postValue(news)
        }
    }
    private fun getType() : List<String> {
        val result = arrayListOf<String>()
        val locations = mItemRepository.getType()
        locations.forEach{
            if (it !in result){
                result.add(it)
            }
        }
        return result.toList()
    }
    fun deleteType(type : String, itemName : String){
        viewModelScope.launch(Dispatchers.IO){
            mItemRepository.deleteType(type, itemName)
        }
    }
    suspend fun checkType(type : String) : Deferred<List<String>> =
        viewModelScope.async(Dispatchers.IO + coroutineException){
            return@async mItemRepository.checkType(type)
        }

    /* 전체 item을 받는 live data */
    fun fetchItems(sortedType : String){
        viewModelScope.launch(Dispatchers.IO){
            count = 0
            val news = toListItems(mItemRepository.getAll(), sortedType)
            itemLiveData.postValue(news)
        }
    }
    fun getItemCount() : Int = count
    private fun toListItems(items : List<Item>, sortedType : String) : List<CheckItemList>{
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
        }
        when(sortedType){
            "addTime" -> {
                result.sortWith(compareBy({it.item.location},{it.order},{it.item.id}))
            }
            "duedate" -> {
                result.sortWith(compareBy({it.item.location},{it.order},{it.item.date}))
            }
            "name" -> {
                result.sortWith(compareBy({it.item.location},{it.order},{it.item.itemName}))
            }
            else -> {
                result.sortWith(compareBy{it.item.location})
            }
        }
        return result
    }

    /* DuedateCheckFragment에서 사용할 데이터 */
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
            if (it.date != ""){
                val targetDate = LocalDate.parse(it.date,baseFormat)
                if (currentDate.isAfter(targetDate)){
                    result.add(it)
                }
            }
        }
        return result
    }

    /* Repository 구현 */
    fun deleteItem(id : Int){
        viewModelScope.launch(Dispatchers.IO){
            mItemRepository.deleteItem(id)
        }
    }
    fun insert(item : Item){
        viewModelScope.launch(Dispatchers.IO){
            mItemRepository.insert(item)
        }
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
        viewModelScope.async(Dispatchers.IO){return@async mItemRepository.searchItem(id) }
    fun updateIsEmpty(id : Int){
        viewModelScope.launch(Dispatchers.IO + coroutineException){
            val item = searchItem(id).await()
            mItemRepository.updateIsEmpty(id, !(item.isEmpty))
        }
    }
    suspend fun checkItem(itemName : String, location : String) : Deferred<Boolean> =
        viewModelScope.async(Dispatchers.IO + coroutineException){
            return@async mItemRepository.checkItem(itemName,location)}
}