package com.example.parttimeduedatemanagement.Event

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.parttimeduedatemanagement.R

class SwipeBackgroundHolder(context : Context) {

    private val defaultItemSidMarginDp = 2f
    private val itemSideMarginUnit =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            defaultItemSidMarginDp,
            context.resources.displayMetrics
        ).toInt()

    var firstItemSideMargin = itemSideMarginUnit
        set(value){
            field = itemSideMarginUnit * (value / defaultItemSidMarginDp).toInt()
        }
    var secondItemSidMargin = itemSideMarginUnit
        set(value){
            field = itemSideMarginUnit * (value / defaultItemSidMarginDp).toInt()
        }
    var firstIcon = ContextCompat.getDrawable(context, R.drawable.sold_out)!! // 첫번째로 나올 사진
    var secondIcon = ContextCompat.getDrawable(context, R.drawable.cancel)!! // 두번째로 나올 사진

    private val firstIntrinsicWidth = firstIcon.intrinsicWidth
    private val firstIntrinsicHeight = firstIcon.intrinsicHeight
    private val secondIntrinsicWidth = secondIcon.intrinsicWidth
    private val secondIntrinsicHeight = secondIcon.intrinsicHeight

    var backgroundColor = Color.parseColor("#000000")
    private val background = ColorDrawable()

    var holderWidth = 0

    fun updateHolderWidth(){
        holderWidth = (firstIntrinsicWidth + 2 * firstItemSideMargin)
    }
    fun isFirstItemArea(x : Int, y : Int) : Boolean{
        return firstIcon.bounds.contains(x,y)
    }
    fun drawHoldingBackground(canvas : Canvas, viewHolder : RecyclerView.ViewHolder, x : Int, isRightToLeft : Boolean, isFirst : Boolean){
        val itemView = viewHolder.itemView

        drawBackground(canvas, itemView, x, isRightToLeft)
        if (isFirst){
            drawFirstItem(canvas,itemView,isRightToLeft)
        } else {
            drawSecondItem(canvas, itemView, isRightToLeft)
        }
    }
    private fun drawBackground(canvas : Canvas, itemView : View, x : Int, isRightToLeft : Boolean){
        background.color = backgroundColor
        if (isRightToLeft){
            background.setBounds(itemView.right + x, itemView.top, itemView.right, itemView.bottom)
        } else {
            background.setBounds(0,itemView.top,x,itemView.bottom)
        }
        background.draw(canvas)
    }
    private fun drawFirstItem(canvas : Canvas, itemView: View, isRightToLeft : Boolean){
        val itemHeight = itemView.bottom - itemView.top

        val firstIconTop = itemView.top + (itemHeight - firstIntrinsicHeight) / 2
        val firstIconBottom = firstIconTop + firstIntrinsicHeight
        val firstIconLeft : Int = if(isRightToLeft){
            itemView.right - firstItemSideMargin - firstIntrinsicWidth
        } else {
            firstItemSideMargin
        }
        val firstIconRight = if (isRightToLeft){
            itemView.right - firstItemSideMargin
        } else {
            firstIconLeft + firstIntrinsicWidth
        }

        firstIcon.setBounds(firstIconLeft, firstIconTop, firstIconRight, firstIconBottom)
        firstIcon.draw(canvas)
    }
    private fun drawSecondItem(canvas: Canvas, itemView: View, isRightToLeft: Boolean){
        val itemHeight = itemView.bottom - itemView.top

        val secondIconTop = itemView.top + (itemHeight - secondIntrinsicHeight) / 2
        val secondIconBottom = secondIconTop + secondIntrinsicHeight
        val secondIconLeft : Int = if(isRightToLeft){
            itemView.right - secondItemSidMargin - secondIntrinsicWidth
        } else {
            secondItemSidMargin
        }
        val secondIconRight = if (isRightToLeft){
            itemView.right - secondItemSidMargin
        } else {
            secondIconLeft + secondIntrinsicWidth
        }

        secondIcon.setBounds(secondIconLeft, secondIconTop, secondIconRight, secondIconBottom)
        secondIcon.draw(canvas)
    }
}