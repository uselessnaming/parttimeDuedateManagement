package com.example.parttimeduedatemanagement.Adapater

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.parttimeduedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.databinding.ItemContainerBinding

class ItemAdapter : RecyclerView.Adapter<ItemViewHolder>(){
    private val TAG : String = "ItemAdapter"
    private lateinit var binding : ItemContainerBinding
    private val items = arrayListOf<Item>()
    private val headers = arrayListOf<String>()

    fun submitList(items : List<Item>){
        this.items.clear()
        this.headers.clear()
        this.items.addAll(items)
        items.forEach{
            if (it.itemName == "") {
                headers.add(it.location)
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = headers.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int) : ItemViewHolder{
        binding = ItemContainerBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ItemViewHolder(binding,headers,items,itemLongClickListener)
    }
    override fun onBindViewHolder(holder : ItemViewHolder, position : Int){
        holder.bind(position)
    }
    fun setItemLongClickListener(onItemLongClickListener : OnItemLongClickListener){
        this.itemLongClickListener = onItemLongClickListener
    }
    private lateinit var itemLongClickListener : OnItemLongClickListener
}

