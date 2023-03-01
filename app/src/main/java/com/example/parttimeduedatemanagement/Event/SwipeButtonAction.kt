package com.example.parttimeduedatemanagement.Event

import androidx.recyclerview.widget.RecyclerView

interface SwipeButtonAction {
    //fun onClickFirstButton(absoluteAdapterPosition : Int)
    fun onClickFirstButton(viewHolder : RecyclerView.ViewHolder)
}