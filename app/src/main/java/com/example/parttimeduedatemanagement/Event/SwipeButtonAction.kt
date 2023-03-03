package com.example.parttimeduedatemanagement.Event

import com.example.parttimeduedatemanagement.Adapater.ItemChildViewHolder

interface SwipeButtonAction {
    //fun onClickFirstButton(absoluteAdapterPosition : Int)
    fun onClickFirstButton(viewHolder : ItemChildViewHolder, isChecked : Boolean)
}