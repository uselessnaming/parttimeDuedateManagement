package com.example.parttimeduedatemanagement.Adapater

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.part_timedatemanagement.ItemViewModel
import com.example.parttimeduedatemanagement.R

private val TAG : String = "ItemContainerAdapter"

class ItemContainerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private lateinit var mItemViewModel : ItemViewModel
    private var locations : List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG,"onCreateViewHolder")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_container,parent,false)
        return ItemContainerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        val itemViewHolder = holder as ItemContainerViewHolder
        itemViewHolder.bind(locations.get(position))
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    inner class ItemContainerViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var itemSerialNumber : TextView = itemView.findViewById(R.id.itemSerialNumber)
        fun bind(location : String){
            itemSerialNumber.text = location
            val itemList : RecyclerView = itemView.findViewById(R.id.itemList)
            val itemAdapter = ItemAdapter()
            itemList.adapter = itemAdapter
        }
    }
    fun setLocations(locations : List<String>){
        this.locations = locations
        notifyDataSetChanged()
    }
}