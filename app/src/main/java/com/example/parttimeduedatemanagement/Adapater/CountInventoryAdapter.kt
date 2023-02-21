package com.example.parttimeduedatemanagement.Adapater

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.parttimeduedatemanagement.Database.CheckItemList
import com.example.parttimeduedatemanagement.Database.EaItem
import com.example.parttimeduedatemanagement.databinding.CountInventoryItemBinding
import com.example.parttimeduedatemanagement.databinding.ItemContainerBinding

class CountInventoryAdapter : RecyclerView.Adapter<CountInventoryViewHolder>() {
    private val TAG: String = "CountInventoryAdapter"
    private val items = arrayListOf<EaItem>()

    fun submitList(items: List<EaItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    private fun getItem(position : Int) : EaItem = items[position]

    override fun getItemCount(): Int = this.items.size

    override fun getItemViewType(position: Int): Int = getItem(position).layoutId

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int,): CountInventoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        Log.d(TAG,"${viewType}")
        return when(viewType) {
            EaItem.Header.VIEW_TYPE -> CountInventoryHeaderViewHolder(itemView)
            EaItem.Child.VIEW_TYPE -> CountInventoryChildViewHolder(itemView)
            else -> throw IllegalArgumentException("Error : Cannot create ViewHolder")
        }
    }
    override fun onBindViewHolder(holder: CountInventoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    fun setOnBtnClickListener(listener : OnBtnClickListener){
        onBtnClickListener = listener
    }
}
interface OnBtnClickListener{
    fun onPlus(id : Int, ea : Int)
    fun onMinus(id : Int, ea : Int)
}
private lateinit var onBtnClickListener : OnBtnClickListener

abstract class CountInventoryViewHolder(
    itemView : View
) : RecyclerView.ViewHolder(itemView){
    abstract fun bind(eaItem : EaItem)
}

class CountInventoryHeaderViewHolder(
    itemView : View
) : CountInventoryViewHolder(itemView){
    private val binding by lazy{
        ItemContainerBinding.bind(itemView)
    }

    override fun bind(eaItem : EaItem){
        val item = (eaItem as EaItem.Header).item
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
    override fun bind(eaItem : EaItem){
        val item = (eaItem as EaItem.Child).item
        binding.apply{
            tvItemName.text = item.itemName
            etCount.setText(item.ea.toString())
            btnMinus.setOnClickListener{
                onBtnClickListener.onMinus(item.id, item.ea)
            }
            btnPlus.setOnClickListener{
                onBtnClickListener.onPlus(item.id, item.ea)
            }
        }
    }
}