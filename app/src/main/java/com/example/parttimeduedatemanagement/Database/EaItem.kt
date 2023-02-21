package com.example.parttimeduedatemanagement.Database

import com.example.part_timedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.R

sealed class EaItem {
    abstract val item: Item
    abstract val layoutId: Int
    abstract val order : Int

    data class Header(
        override val item: Item,
        override val layoutId: Int = VIEW_TYPE,
        override val order : Int = 0
    ) : EaItem() {
        companion object {
            const val VIEW_TYPE = R.layout.item_container
        }
    }

    data class Child(
        override val item: Item,
        override val layoutId: Int = VIEW_TYPE,
        override val order : Int = 1
    ) : EaItem() {
        companion object {
            const val VIEW_TYPE = R.layout.count_inventory_item
        }
    }
}