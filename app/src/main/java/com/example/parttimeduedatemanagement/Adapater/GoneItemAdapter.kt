package com.example.parttimeduedatemanagement.Adapater

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.parttimeduedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.databinding.GoneItemContainerBinding

class GoneItemAdapter : RecyclerView.Adapter<GoneItemViewHolder>(){
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
            Log.d("AAAAA","item[position] - ${items[position]}")
            goneItemClickListener.onClick(items[position])
        }
    }

    fun setGoneItemClickListener(goneItemClickListener : OnItemClickListener){
        this.goneItemClickListener = goneItemClickListener
    }

    private lateinit var goneItemClickListener : OnItemClickListener
}