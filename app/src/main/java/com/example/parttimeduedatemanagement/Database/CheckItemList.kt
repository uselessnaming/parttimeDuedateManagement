package com.example.parttimeduedatemanagement.Database

import com.example.part_timedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.R

sealed class CheckItemList{
    abstract val item : Item
    abstract val layoutId : Int

    data class Header(
        override val item : Item,
        override val layoutId : Int = VIEW_TYPE
    ) : CheckItemList(){
        companion object{
            const val VIEW_TYPE = R.layout.item_container
        }
    }

    data class Child(
        override val item : Item,
        override val layoutId : Int = VIEW_TYPE
    ) : CheckItemList(){
        companion object{
            const val VIEW_TYPE = R.layout.item
        }
    }
}
