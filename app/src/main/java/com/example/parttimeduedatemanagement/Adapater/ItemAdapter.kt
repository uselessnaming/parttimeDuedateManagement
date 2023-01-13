package com.example.parttimeduedatemanagement.Adapater

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.part_timedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.R

private val TAG : String = "ItemAdapter"
class ItemAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items : List<Item> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG,"onCreateViewHolder")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item,parent,false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(TAG,"onBindViewHolder")
        val item = items[position]
        val itemViewHolder = holder as ItemViewHolder
        itemViewHolder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ItemViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val item_name : TextView = itemView.findViewById(R.id.itemName)
        val due_date : TextView = itemView.findViewById(R.id.itemDuedate)
        fun bind(item : Item){
            item_name?.text = item.itemName
            due_date?.text = item.date
        }
    }

    fun setItems(items : List<Item>){
        this.items = items
        notifyDataSetChanged()
    }
}