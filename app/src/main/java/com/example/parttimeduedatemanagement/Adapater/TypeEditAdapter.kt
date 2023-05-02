package com.example.parttimeduedatemanagement.Adapater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.parttimeduedatemanagement.databinding.TypeEditRecyclerItemBinding

class TypeEditAdapter : RecyclerView.Adapter<TypeEditViewHolder>(){
    private val TAG : String = "TypeEditAdapter"
    private val types = arrayListOf<String>()
    private lateinit var binding : TypeEditRecyclerItemBinding

    fun submitList(types : List<String>){
        this.types.clear()
        this.types.addAll(types)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = this.types.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int) : TypeEditViewHolder {
        binding = TypeEditRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TypeEditViewHolder(binding)
    }
    override fun onBindViewHolder(holder : TypeEditViewHolder, position : Int){
        holder.bind(types[position])
        binding.imgDelete.setOnClickListener{
            onClickListener.onClick(types[position])
        }
    }
    private lateinit var onClickListener : OnItemClickListener
    fun setOnClickListener(onClickListener : OnItemClickListener){
        this.onClickListener = onClickListener
    }
}