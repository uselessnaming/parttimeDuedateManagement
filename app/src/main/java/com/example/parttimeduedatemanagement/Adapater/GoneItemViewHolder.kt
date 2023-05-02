package com.example.parttimeduedatemanagement.Adapater

import androidx.recyclerview.widget.RecyclerView
import com.example.parttimeduedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.databinding.GoneItemContainerBinding

class GoneItemViewHolder(private val binding : GoneItemContainerBinding) : RecyclerView.ViewHolder(binding.root){

    fun bind(item : Item){

        binding.tvItemName.text = item.itemName
        binding.tvItemDate.text = item.date
        binding.tvItemLocation.text = item.location
    }
}