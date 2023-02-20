package com.example.parttimeduedatemanagement.Adapater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.part_timedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.Database.CheckItemList
import com.example.parttimeduedatemanagement.databinding.CountInventoryItemBinding
import com.example.parttimeduedatemanagement.databinding.ItemBinding
import com.example.parttimeduedatemanagement.databinding.ItemContainerBinding

class CountInventoryAdapter : RecyclerView.Adapter<CountInventoryViewHolder>() {
    private val TAG: String = "CountInventoryAdapter"
    private val items = arrayListOf<CheckItemList>()

    fun submitList(items: List<CheckItemList>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = this.items.size

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int,): CountInventoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when(viewType) {
            CheckItemList.Header.VIEW_TYPE -> CountInventoryChildViewHolder(itemView)
            CheckItemList.Child.VIEW_TYPE -> CountInventoryHeaderViewHolder(itemView)
            else -> throw IllegalArgumentException("Error : Cannot create ViewHolder")
        }
    }
    override fun onBindViewHolder(holder: CountInventoryViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

abstract class CountInventoryViewHolder(
    itemView : View
) : RecyclerView.ViewHolder(itemView){
    abstract fun bind(itemList : CheckItemList)
}

class CountInventoryHeaderViewHolder(
    itemView : View
) : CountInventoryViewHolder(itemView){
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

class CountInventoryChildViewHolder(
    itemView : View
) : CountInventoryViewHolder(itemView){
    private val binding by lazy{
        CountInventoryItemBinding.bind(itemView)
    }
    override fun bind(itemList : CheckItemList){
        val item = (itemList as CheckItemList.Child).item
        binding.apply{
            tvItemName.text = item.itemName
            btnMinus.setOnClickListener{

            }
            btnPlus.setOnClickListener{

            }
        }
    }
}