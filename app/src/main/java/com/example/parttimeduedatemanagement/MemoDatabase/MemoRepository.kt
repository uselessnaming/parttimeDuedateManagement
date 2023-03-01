package com.example.parttimeduedatemanagement.MemoDatabase

import android.app.Application
import android.content.Context

class MemoRepository(application : Application) {
    private var mMemoDatabase : MemoDatabase
    private var mMemoDao : MemoDao

    init{
        mMemoDatabase = MemoDatabase.getInstance(application)
        mMemoDao = mMemoDatabase.memoDao()
    }

    fun getAll() : List<Memo>{
        return mMemoDao.getAll()
    }
    fun insert(memo : Memo){
        mMemoDao.insertMemo(memo)
    }
    fun updateMemo(id : Int, memo : Memo){
        mMemoDao.updateMemo(id, memo.title, memo.content, memo.date)
    }
    fun deleteMemo(id : Int){
        mMemoDao.deleteMemo(id)
    }
    fun isTitle(title : String) : Boolean{
        return mMemoDao.isTitle(title)
    }
    fun searchId(title : String) : Int{
        return mMemoDao.searchId(title)
    }
    fun searchMemo(id : Int) : Memo? {
        return mMemoDao.searchMemo(id)
    }

    companion object{
        private var INSTANCE : MemoRepository? = null

        fun get(application : Application) : MemoRepository{
            return INSTANCE ?: MemoRepository(application)
        }
    }
}