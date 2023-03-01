package com.example.parttimeduedatemanagement.Adapater

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.part_timedatemanagement.Database.Item
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
    fun setOnEditorActionListener(listener : OnEditorActionListener){
        onEditorActionListener = listener
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

interface OnEditorActionListener{
    fun onClickEnter(item : Item, ea :Int)
}
private lateinit var onEditorActionListener : OnEditorActionListener
class CountInventoryChildViewHolder(
    itemView : View
) : CountInventoryViewHolder(itemView){
    private val binding by lazy{ CountInventoryItemBinding.bind(itemView) }
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
            etCount.setOnEditorActionListener { _, _, _ ->
                Log.d("CountInventoryAdapter","onEditorActionListener")
                onEditorActionListener.onClickEnter(item, etCount.text.toString().toInt())
                true
            }
        }
    }
}