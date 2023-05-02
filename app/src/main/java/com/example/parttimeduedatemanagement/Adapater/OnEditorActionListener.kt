package com.example.parttimeduedatemanagement.Adapater

import com.example.parttimeduedatemanagement.Database.Item

interface OnEditorActionListener {
    fun onEnterClick(item : Item, ea :Int)
}