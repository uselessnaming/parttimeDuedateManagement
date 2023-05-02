package com.example.parttimeduedatemanagement.Adapater

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.parttimeduedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.R
import com.example.parttimeduedatemanagement.databinding.ItemContainerBinding

class CountInventoryAdapter : RecyclerView.Adapter<CountInventoryViewHolder>() {
    private val TAG: String = "CountInventoryAdapter"
    private val items = arrayListOf<Item>()
    private val headers = arrayListOf<String>()
    private lateinit var binding : ItemContainerBinding

    /* 나중에 설정에서는 exceptTarget을 개인적으로 설정할 수 있도록 */
    private val essentialTarget = listOf("묶음라면","커피","차","씨리얼","과자","음료 캔","음료 작은펱","음료 큰펱","냉장")

    fun submitList(items: List<Item>) {
        this.items.clear()
        this.headers.clear()
        this.items.addAll(items)
        var location = ""
        items.forEach{
            if (location != it.location && it.location in essentialTarget){
                headers.add(it.location)
                location = it.location
            }
        }
        notifyDataSetChanged()
    }
    fun setOnBtnClickListener(listener : OnBtnClickListener){
        onBtnClickListener = listener
    }
    fun setOnEditorActionListener(listener : OnEditorActionListener){
        onEditorActionListener = listener
    }
    private lateinit var onBtnClickListener : OnBtnClickListener

    private lateinit var onEditorActionListener : OnEditorActionListener

    override fun getItemCount(): Int = headers.size

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int,): CountInventoryViewHolder {
        binding = ItemContainerBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return CountInventoryViewHolder(binding,headers,items,onBtnClickListener,onEditorActionListener)
    }
    override fun onBindViewHolder(holder: CountInventoryViewHolder, position: Int) {
        holder.bind(position)
    }
}