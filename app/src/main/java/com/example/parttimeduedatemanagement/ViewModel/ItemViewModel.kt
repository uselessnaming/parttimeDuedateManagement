package com.example.parttimeduedatemanagement.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.parttimeduedatemanagement.Database.Item
import com.example.part_timedatemanagement.Database.ItemRepository
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ItemViewModel(application : Application) : AndroidViewModel(application){
    private val mItemRepository : ItemRepository = ItemRepository.get(application)

    private var typeLiveData = MutableLiveData<List<String>>()
    val types : LiveData<List<String>> get() = typeLiveData

    private val sb = StringBuilder()
    private var curLocation : String = ""

    private var itemLiveData = MutableLiveData<List<Item>>()
    val items : LiveData<List<Item>> get() = itemLiveData
    var count : Int = 0

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
        viewModelScope.launch(Dispatchers.IO) {
            count = 0
            val news = toListItems(mItemRepository.getAll(),sortedType)
            itemLiveData.postValue(news)
        }
    }
    fun getItemCount() : Int = count
    private fun toListItems(items : List<Item>, sortedType : String) : List<Item>{
        val result = arrayListOf<Item>()
        val headers = arrayListOf<String>()

        items.forEach{
            if (it.location !in headers){
                if (it.itemName == "" && it.date == ""){
                    headers.add(it.location)
                }
                result.add(it)
            }
            if (it.itemName!= "" ){
                count += 1
                result.add(it)
            }
        }
        when(sortedType){
            "addTime" -> {
                result.sortWith(compareBy({it.location},{it.id}))
            }
            "duedate" -> {
                result.sortWith(compareBy({it.location},{it.date}))
            }
            "name" -> {
                result.sortWith(compareBy({it.location},{it.itemName}))
            }
            "stringBuilder" -> {
                result.sortWith(compareBy({it.location},{it.id}))
                setStringBuilder(result)
            }
            else -> {
                result.sortWith(compareBy{it.location})
            }
        }
        return result
    }
    private fun setStringBuilder(result : ArrayList<Item>){
        sb.clear()
        curLocation = ""
        result.forEach{
            if (it.itemName != ""){
                if (it.ea != 0){
                    if (curLocation != it.location){
                        curLocation = it.location
                        sb.append("\n${it.location} : ")
                    }
                    sb.append("${it.itemName} - ${it.ea}  ")
                }
            }
        }
    }
    fun getStringBuilder() : StringBuilder = sb

    /* DuedateCheckFragment에서 사용할 데이터 */
    private var goneItemLiveData = MutableLiveData<List<Item>>()
    val goneItems : LiveData<List<Item>> get() = goneItemLiveData
    private val coroutineException = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }
    fun goneItemsFetch(today : String){
        viewModelScope.launch(Dispatchers.IO + coroutineException){
            val news = getGoneItems(mItemRepository.getAll(),today,"goneItem")
            goneItemLiveData.postValue(news)
        }
    }
    private fun getGoneItems(items : List<Item>, today : String, type : String) : List<Item>{
        val result = arrayListOf<Item>()

        when (type) {
            "goneItem" -> {
                val baseFormat = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
                val currentDate = LocalDate.parse(today,baseFormat)
                items.forEach{
                    if (it.date != ""){
                        val targetDate = LocalDate.parse(it.date,baseFormat)
                        if (!currentDate.isBefore(targetDate)){
                            result.add(it)
                        }
                    }
                }
            }
            "soldOut" -> {
                items.forEach {
                    if (it.date.isEmpty() && it.itemName.isNotBlank()) {
                        result.add(it)
                    }
                }
                result.sortWith(compareBy{it.location})
            }
            else -> {
                throw NullPointerException("The Type is NONE")
            }
        }
        return result
    }

    /* SoldOutCheckFragment의 adapter 아이템들 구성 */
    private var soldOutLD = MutableLiveData<List<Item>>()
    val soldOutList : LiveData<List<Item>> get() = soldOutLD
    fun fetchSoldOutList(){
        viewModelScope.launch(Dispatchers.IO + coroutineException){
            val soldOuts = getGoneItems(mItemRepository.getAll(), "", "soldOut")
            soldOutLD.postValue(soldOuts)
        }
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
    fun updateIsEmpty(id : Int, isEmpty : Boolean){
        viewModelScope.launch(Dispatchers.IO + coroutineException){
            mItemRepository.updateIsEmpty(id, isEmpty)
        }
    }
    suspend fun checkItem(itemName : String, location : String) : Deferred<Boolean> =
        viewModelScope.async(Dispatchers.IO + coroutineException){
            return@async mItemRepository.checkItem(itemName,location)}

    fun updateEA(id : Int, ea : Int){
        viewModelScope.launch(Dispatchers.IO + coroutineException){
            mItemRepository.updateEA(id, ea)
        }
    }
    fun resetEA(){
        viewModelScope.launch(Dispatchers.IO){
            mItemRepository.resetEA()
        }
    }
}