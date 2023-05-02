package com.example.parttimeduedatemanagement.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.parttimeduedatemanagement.MemoDatabase.Memo
import com.example.parttimeduedatemanagement.MemoDatabase.MemoRepository
import kotlinx.coroutines.*

class MemoViewModel(application : Application) : AndroidViewModel(application) {
    private val TAG = "MemoViewModel"
    private val mMemoRepository : MemoRepository = MemoRepository.get(application)
    private val coroutineException = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }

    private var memoLiveData = MutableLiveData<List<Memo>>()
    val memos : LiveData<List<Memo>> get() = memoLiveData

    fun fetchMemos(){
        viewModelScope.launch(Dispatchers.IO + coroutineException){
            val news = mMemoRepository.getAll()
            memoLiveData.postValue(news)
        }
    }
    fun insertMemo(memo : Memo){
        viewModelScope.launch(Dispatchers.IO + coroutineException){
            mMemoRepository.insert(memo)
        }
    }
    fun updateMemo(id : Int, memo : Memo){
        viewModelScope.launch(Dispatchers.IO + coroutineException){
            mMemoRepository.updateMemo(id, memo)
        }
    }
    fun deleteMemo(id : Int){
        viewModelScope.launch(Dispatchers.IO + coroutineException){
            mMemoRepository.deleteMemo(id)
        }
    }
    suspend fun isTitle(title : String) : Deferred<Boolean> = viewModelScope.async(Dispatchers.IO + coroutineException){
        return@async mMemoRepository.isTitle(title)
    }
    suspend fun searchId(title : String) : Deferred<Int> = viewModelScope.async(Dispatchers.IO + coroutineException){
        return@async mMemoRepository.searchId(title)
    }
    suspend fun searchMemo(id : Int) : Deferred<Memo> = viewModelScope.async(Dispatchers.IO + coroutineException){
        val memo : Memo? = mMemoRepository.searchMemo(id) ?: Memo("","","")
        return@async memo!!
    }
}