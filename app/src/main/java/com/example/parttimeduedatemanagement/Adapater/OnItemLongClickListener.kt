package com.example.parttimeduedatemanagement.Adapater

import android.view.View
import com.example.parttimeduedatemanagement.Database.Item

interface OnItemLongClickListener {
    fun onLongClick(v : View, item : Item)
}