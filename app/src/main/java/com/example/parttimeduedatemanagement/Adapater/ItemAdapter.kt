package com.example.parttimeduedatemanagement.Adapater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.part_timedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.Database.CheckItemList
import com.example.parttimeduedatemanagement.HomeFragment
import com.example.parttimeduedatemanagement.databinding.ItemBinding
import com.example.parttimeduedatemanagement.databinding.ItemContainerBinding

class ItemAdapter : RecyclerView.Adapter<ItemViewHolder>(){
    private val TAG : String = "ItemAdapter"
    private val items = arrayListOf<CheckItemList>()

    fun submitList(items : List<CheckItemList>){
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    private fun getItem(position:Int) : CheckItemList = this.items[position]

    override fun getItemCount(): Int = this.items.size

    override fun getItemViewType(position: Int): Int = getItem(position).layoutId

    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int) : ItemViewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when(viewType){
            CheckItemList.Header.VIEW_TYPE -> ItemHeaderViewHolder(itemView)
            CheckItemList.Child.VIEW_TYPE -> ItemChildViewHolder(itemView)
            else -> throw IllegalArgumentException("Cannot create ViewHolder")
        }
    }
    override fun onBindViewHolder(holder : ItemViewHolder, position : Int){
        holder.bind(getItem(position))

        if (holder is ItemChildViewHolder){
            holder.itemView.setOnLongClickListener{
                itemLongClickListener.onLongClick(it,getItem(position))
                return@setOnLongClickListener(true)
            }
        }
    }

    interface OnItemLongClickListener{
        fun onLongClick(v : View, id : CheckItemList)
    }
    fun setItemLongClickListener(onItemLongClickListener : OnItemLongClickListener){
        this.itemLongClickListener = onItemLongClickListener
    }
    private lateinit var itemLongClickListener : OnItemLongClickListener
}

abstract class ItemViewHolder(
    itemView : View
) : RecyclerView.ViewHolder(itemView){
    abstract fun bind(itemList : CheckItemList)
}

class ItemHeaderViewHolder(
    itemView : View
) : ItemViewHolder(itemView){
    private val binding by lazy{
        ItemContainerBinding.bind(itemView)
    }

    override fun bind(itemList : CheckItemList){
        val item = (itemList as CheckItemList.Header).item
        binding.apply{
            itemLocation.text = "종류 : " + item.location
        }
    }
}

class ItemChildViewHolder(
    itemView : View
) : ItemViewHolder(itemView){
    private val binding by lazy{ItemBinding.bind(itemView)}
    var id : Int = 0
    override fun bind(itemList : CheckItemList){
        val item = (itemList as CheckItemList.Child).item
        id = item.id
        binding.apply{
            itemName.text = item.itemName
            itemDuedate.text = item.date
            setImageTag(item.isEmpty)
        }
    }
    fun setImageTag(isSoldOut : Boolean){
        if (isSoldOut){
            binding.imgSoldOutTag.visibility = View.VISIBLE
        } else {
            binding.imgSoldOutTag.visibility = View.GONE
        }
    }
    fun getBindingId() : Int {return id}
}

