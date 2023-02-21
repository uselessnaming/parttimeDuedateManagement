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

    companion object{
        private var INSTANCE : MemoRepository? = null

        fun get(application : Application) : MemoRepository{
            return INSTANCE ?: MemoRepository(application)
        }
    }
}