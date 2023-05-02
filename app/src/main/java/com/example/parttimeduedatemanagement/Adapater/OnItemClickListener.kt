package com.example.parttimeduedatemanagement.Adapater

import com.example.parttimeduedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.MemoDatabase.Memo

interface OnItemClickListener {
    abstract fun onClick(item : Item)
    abstract fun onClick(memo : Memo)
    abstract fun onClick(typeHeader : String)
}