package com.example.parttimeduedatemanagement.Adapater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.part_timedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.databinding.GoneItemContainerBinding
import com.example.parttimeduedatemanagement.databinding.TypeEditRecyclerItemBinding
import kotlinx.coroutines.NonDisposableHandle.parent

class TypeEditAdapter : RecyclerView.Adapter<TypeEditAdapter.TypeEditViewHolder>(){
    private val TAG : String = "TypeEditAdapter"
    private val types = arrayListOf<String>()
    private lateinit var binding : TypeEditRecyclerItemBinding

    fun submitList(types : List<String>){
        this.types.clear()
        this.types.addAll(types)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = this.types.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int) : TypeEditAdapter.TypeEditViewHolder {
        binding = TypeEditRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TypeEditViewHolder(binding)
    }
    override fun onBindViewHolder(holder : TypeEditAdapter.TypeEditViewHolder, position : Int){
        holder.bind(types[position])
        binding.imgDelete.setOnClickListener{
            onClickListener.onClick(types[position])
        }
    }

    inner class TypeEditViewHolder(binding : TypeEditRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(type : String){
            binding.tvType.text = type
        }
    }
    interface OnClickListener{
        fun onClick(typesHeader : String)
    }
    private lateinit var onClickListener : OnClickListener
    fun setOnClickListener(onClickListener : OnClickListener){
        this.onClickListener = onClickListener
    }
}