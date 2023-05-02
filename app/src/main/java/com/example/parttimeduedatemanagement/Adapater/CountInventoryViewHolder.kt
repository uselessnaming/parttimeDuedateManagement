package com.example.parttimeduedatemanagement.Adapater

import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.parttimeduedatemanagement.Database.Item
import com.example.parttimeduedatemanagement.R
import com.example.parttimeduedatemanagement.databinding.ItemContainerBinding

class CountInventoryViewHolder(private var binding : ItemContainerBinding, private val headers : ArrayList<String>, private val items : ArrayList<Item>,
                               private var onBtnClickListener: OnBtnClickListener, private var onEditorActionListener: OnEditorActionListener)
    : RecyclerView.ViewHolder(binding.root){
    init{
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
        binding.apply{
            tvItemLocation.text = headers[pos]
            lyDetail.removeAllViews()
            items.forEach{ item ->
                if (item.itemName != ""){
                    if (item.location == headers[pos]){
                        val constraintLayout = ConstraintLayout(binding.root.context).apply{
                            layoutParams = ConstraintLayout.LayoutParams(
                                ConstraintLayout.LayoutParams.MATCH_PARENT,
                                ConstraintLayout.LayoutParams.WRAP_CONTENT)
                        }
                        val tvItemName = TextView(binding.root.context).apply{
                            text = item.itemName
                            textSize = 30f
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.MATCH_PARENT)
                            setTextColor(ContextCompat.getColor(context, R.color.itemTextColor))
                            id = View.generateViewId()
                        }
                        val minusButton = ImageButton(binding.root.context).apply{
                            setImageResource(android.R.drawable.ic_media_rew)
                            layoutParams = ViewGroup.LayoutParams(70, 70)
                            id = View.generateViewId()
                            setOnClickListener{
                                onBtnClickListener.onMinus(item.id,item.ea)
                            }
                        }
                        val plusButton = ImageButton(binding.root.context).apply{
                            setImageResource(android.R.drawable.ic_media_ff)
                            layoutParams = ViewGroup.LayoutParams(70,70)
                            id = View.generateViewId()
                            setOnClickListener{
                                onBtnClickListener.onPlus(item.id,item.ea)
                            }
                        }
                        val etEA = EditText(binding.root.context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT)
                            id = View.generateViewId()
                            maxLines = 1
                            isSingleLine = true
                            setTextColor(ContextCompat.getColor(context, R.color.itemTextColor))
                            setText(item.ea.toString())
                            hint = "개수"
                            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                            imeOptions = EditorInfo.IME_ACTION_DONE
                            setOnEditorActionListener { _, _, _ ->
                                onEditorActionListener.onEnterClick(item,this.text.toString().toInt())
                                clearFocus()
                                return@setOnEditorActionListener true
                            }
                        }
                        constraintLayout.addView(tvItemName)
                        constraintLayout.addView(plusButton)
                        constraintLayout.addView(etEA)
                        constraintLayout.addView(minusButton)

                        val constraintSet = ConstraintSet()
                        constraintSet.clone(constraintLayout)
                        /* tvItemName */
                        constraintSet.connect(
                            tvItemName.id,
                            ConstraintSet.START,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.START,
                            15
                        )
                        constraintSet.connect(
                            tvItemName.id,
                            ConstraintSet.TOP,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.TOP,
                            5
                        )
                        constraintSet.connect(
                            tvItemName.id,
                            ConstraintSet.BOTTOM,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.BOTTOM,
                            5
                        )
                        /* plusButton */
                        constraintSet.connect(
                            plusButton.id,
                            ConstraintSet.END,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.END,
                            15
                        )
                        constraintSet.connect(
                            plusButton.id,
                            ConstraintSet.TOP,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.TOP,
                            15
                        )
                        constraintSet.connect(
                            plusButton.id,
                            ConstraintSet.BOTTOM,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.BOTTOM,
                            15
                        )
                        /* etEA */
                        constraintSet.connect(
                            etEA.id,
                            ConstraintSet.END,
                            plusButton.id,
                            ConstraintSet.START,
                            10
                        )
                        constraintSet.connect(
                            etEA.id,
                            ConstraintSet.TOP,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.TOP,
                            15
                        )
                        constraintSet.connect(
                            etEA.id,
                            ConstraintSet.BOTTOM,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.BOTTOM,
                            15
                        )
                        /* minusButton */
                        constraintSet.connect(
                            minusButton.id,
                            ConstraintSet.END,
                            etEA.id,
                            ConstraintSet.START,
                            10
                        )
                        constraintSet.connect(
                            minusButton.id,
                            ConstraintSet.TOP,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.TOP,
                            15
                        )
                        constraintSet.connect(
                            minusButton.id,
                            ConstraintSet.BOTTOM,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.BOTTOM,
                            15
                        )
                        constraintSet.applyTo(constraintLayout)
                        binding.lyDetail.addView(constraintLayout)
                    }
                }
            }
        }
    }
}