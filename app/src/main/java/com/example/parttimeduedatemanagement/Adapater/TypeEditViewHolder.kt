package com.example.parttimeduedatemanagement.Adapater

import androidx.recyclerview.widget.RecyclerView
import com.example.parttimeduedatemanagement.databinding.TypeEditRecyclerItemBinding

class TypeEditViewHolder(private var binding : TypeEditRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){

    fun bind(type : String){
        binding.tvType.text = type
    }
}