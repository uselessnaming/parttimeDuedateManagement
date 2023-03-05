package com.example.parttimeduedatemanagement.Adapater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.part_timedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.databinding.GoneItemContainerBinding

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

        holder.itemView.setOnClickListener{
            goneItemClickListener.onClick(items[position])
        }
    }

    interface OnGoneItemClickListener{
        fun onClick(item : Item)
    }
    fun setGoneItemClickListener(goneItemClickListener : OnGoneItemClickListener){
        this.goneItemClickListener = goneItemClickListener
    }

    private lateinit var goneItemClickListener : OnGoneItemClickListener

    inner class GoneItemViewHolder(binding : GoneItemContainerBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : Item){
            binding.goneItemName.text = item.itemName
            binding.goneItemDate.text = item.date
        }
    }
}