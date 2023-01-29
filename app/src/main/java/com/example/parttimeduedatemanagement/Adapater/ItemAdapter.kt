package com.example.parttimeduedatemanagement.Adapater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parttimeduedatemanagement.Database.CheckItemList
import com.example.parttimeduedatemanagement.R
import com.example.parttimeduedatemanagement.databinding.ItemBinding
import com.example.parttimeduedatemanagement.databinding.ItemContainerBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ItemAdapter(items : List<CheckItemList> = emptyList()) : RecyclerView.Adapter<ItemViewHolder>(){
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

        holder.itemView.setOnLongClickListener{
            itemLongClickListener.onLongClick(it,getItem(position))
            return@setOnLongClickListener(true)
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
            itemLocation.text = item.location
        }
    }
}

class ItemChildViewHolder(
    itemView : View
) : ItemViewHolder(itemView){
    private val binding by lazy{
        ItemBinding.bind(itemView)
    }
    override fun bind(itemList : CheckItemList){
        val item = (itemList as CheckItemList.Child).item

        binding.apply{
            itemName.text = item.itemName
            itemDuedate.text = item.date
        }
    }
}