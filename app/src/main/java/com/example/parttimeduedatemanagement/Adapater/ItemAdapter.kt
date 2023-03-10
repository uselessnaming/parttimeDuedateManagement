package com.example.parttimeduedatemanagement.Adapater

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.parttimeduedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.R
import com.example.parttimeduedatemanagement.databinding.ItemContainerBinding

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(){
    private val TAG : String = "ItemAdapter"
    private lateinit var binding : ItemContainerBinding
    private val items = arrayListOf<Item>()
    private val headers = arrayListOf<String>()

    fun submitList(items : List<Item>){
        this.items.clear()
        this.headers.clear()
        this.items.addAll(items)
        items.forEach{
            if (it.itemName == "") headers.add(it.location)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = headers.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int) : ItemViewHolder{
        binding = ItemContainerBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ItemViewHolder(binding)
    }
    override fun onBindViewHolder(holder : ItemViewHolder, position : Int){
        holder.bind(position)
    }

    interface OnItemLongClickListener{
        fun onLongClick(v : View, item : Item)
    }
    fun setItemLongClickListener(onItemLongClickListener : OnItemLongClickListener){
        this.itemLongClickListener = onItemLongClickListener
    }
    private lateinit var itemLongClickListener : OnItemLongClickListener
    inner class ItemViewHolder(private val binding : ItemContainerBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.cvCardView.setOnClickListener{
                if (binding.lyDetail.visibility == View.VISIBLE){
                    binding.lyDetail.visibility = View.GONE
                    binding.imgImageButton.animate().apply{
                        duration = 200
                        rotation(0f)
                    }
                } else {
                    binding.lyDetail.visibility = View.VISIBLE
                    binding.imgImageButton.animate().apply{
                        duration = 200
                        rotation(180f)
                    }
                }
            }
        }
        fun bind(pos : Int){
            binding.tvItemLocation.text = headers[pos]
            binding.lyDetail.removeAllViews()
            items.forEach{ item ->
                if (item.itemName != ""){
                    if (item.location == headers[pos]){
                        val constraintLayout = ConstraintLayout(binding.root.context).apply{
                            layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.WRAP_CONTENT)
                        }
                        val viewItemName = TextView(binding.root.context).apply {
                            text = "${item.itemName}"
                            textSize = 25f
                            setPadding(10, 10, 5, 10)
                            setBackgroundResource(R.color.layoutBackground)
                            setTextColor(ContextCompat.getColor(context, R.color.itemTextColor))
                            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                            id = View.generateViewId()
                        }
                        val viewItemDate = TextView(binding.root.context).apply {
                            text = if (item.date == "")"?????? ????????? ?????????????????????." else "${item.date}"
                            textSize = 10f
                            setPadding(10, 10, 5, 10)
                            setBackgroundResource(R.color.layoutBackground)
                            setTextColor(ContextCompat.getColor(context, R.color.itemTextColor))
                            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                            id = View.generateViewId()
                        }
                        val imgImageView = ImageView(binding.root.context).apply{
                            setImageResource(R.drawable.item_background)
                            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                            id = View.generateViewId()
                        }
                        viewItemName.setOnLongClickListener{
                            itemLongClickListener.onLongClick(binding.root,item)
                            return@setOnLongClickListener true
                        }
                        constraintLayout.addView(viewItemName)
                        constraintLayout.addView(viewItemDate)
                        constraintLayout.addView(imgImageView)
                        val constraintSet = ConstraintSet()
                        var viewSoldTag: ImageView? = null
                        if (item.isEmpty){
                            viewSoldTag = ImageView(binding.root.context).apply{
                                val imageLayoutParams = LinearLayout.LayoutParams(120,100)
                                imageLayoutParams.gravity = Gravity.END
                                imageLayoutParams.setMargins(0,10,20,0)
                                layoutParams = imageLayoutParams
                                setImageResource(R.drawable.sold_out)
                                id = View.generateViewId()
                            }
                            constraintLayout.addView(viewSoldTag)
                        }
                        constraintSet.clone(constraintLayout)

                        /* viewItemName */
                        constraintSet.connect(
                            viewItemName.id,
                            ConstraintSet.TOP,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.TOP,
                            10
                        )
                        constraintSet.connect(
                            viewItemName.id,
                            ConstraintSet.START,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.START,
                            15
                        )
                        /* viewItemDate */
                        constraintSet.connect(
                            viewItemDate.id,
                            ConstraintSet.TOP,
                            viewItemName.id,
                            ConstraintSet.BOTTOM,
                            10
                        )
                        constraintSet.connect(
                            viewItemDate.id,
                            ConstraintSet.START,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.START,
                            15
                        )
                        /* imgImageView */
                        constraintSet.connect(
                            imgImageView.id,
                            ConstraintSet.TOP,
                            viewItemDate.id,
                            ConstraintSet.BOTTOM
                        )
                        if (viewSoldTag != null){
                            constraintSet.connect(
                                viewSoldTag.id,
                                ConstraintSet.TOP,
                                ConstraintSet.PARENT_ID,
                                ConstraintSet.TOP,
                                20
                            )
                            constraintSet.connect(
                                viewSoldTag.id,
                                ConstraintSet.END,
                                ConstraintSet.PARENT_ID,
                                ConstraintSet.END,
                                20
                            )
                        }
                        constraintSet.applyTo(constraintLayout)
                        binding.lyDetail.addView(constraintLayout)
                    }
                }
            }
        }
    }
}

