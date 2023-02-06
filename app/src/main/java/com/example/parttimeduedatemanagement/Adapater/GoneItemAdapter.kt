package com.example.parttimeduedatemanagement.Adapater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.part_timedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.databinding.GoneItemContainerBinding
import com.example.parttimeduedatemanagement.databinding.ItemContainerBinding

class GoneItemAdapter : RecyclerView.Adapter<GoneItemAdapter.GoneItemViewHolder>(){
    private val TAG : String = "ItemAdapter"
    private val items = arrayListOf<Item>()
    private lateinit var binding : GoneItemContainerBinding

    fun submitList(items : List<Item>){
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = this.items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int) : GoneItemViewHolder{
        binding = GoneItemContainerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return GoneItemViewHolder(binding)
    }
    override fun onBindViewHolder(holder : GoneItemViewHolder, position : Int){
        holder.bind(items[position])

        holder.itemView.setOnLongClickListener{
            itemLongClickListener.onLongClick(it,items[position].id)
            return@setOnLongClickListener(true)
        }
    }

    interface OnItemLongClickListener{
        fun onLongClick(v : View, itemId : Int)
    }
    fun setItemLongClickListener(onItemLongClickListener : OnItemLongClickListener){
        this.itemLongClickListener = onItemLongClickListener
    }

    private lateinit var itemLongClickListener : OnItemLongClickListener

    inner class GoneItemViewHolder(binding : GoneItemContainerBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : Item){
            binding.goneItemName.text = item.itemName
            binding.goneItemDate.text = item.date
        }
    }
}