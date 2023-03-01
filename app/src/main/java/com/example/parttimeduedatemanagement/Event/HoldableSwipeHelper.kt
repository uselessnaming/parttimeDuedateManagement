package com.example.parttimeduedatemanagement.Event

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import android.graphics.Canvas
import android.util.Log
import android.view.MotionEvent
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.parttimeduedatemanagement.Adapater.ItemChildViewHolder
import com.example.parttimeduedatemanagement.Adapater.ItemHeaderViewHolder
import com.example.parttimeduedatemanagement.ViewModel.ItemViewModel
import kotlin.math.max
import kotlin.math.min

class HoldableSwipeHelper private constructor(builder : Builder) : ItemTouchHelper.Callback() {
    private var swipeBackgroundHolder = builder.swipeBackgroundHolder
    private val buttonAction = builder.buttonAction!!
    private var firstItemDismissFlag = builder.firstItemDismissFlag
    private val excludeViewTypeSet : Set<Int> = builder.excludeViewTypeSet
    private var isRightToLeft = builder.isRightToLeft
    private var currentViewHolder : RecyclerView.ViewHolder? = null
    private var absoluteDX = 0f
    private var scopedX = 0f
    private var isFirst = builder.isFirst

    init {
        addRecyclerViewDecoration(builder.recyclerView!!)
        addRecyclerViewListener(builder.recyclerView!!)

        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(builder.recyclerView!!)

    }

    class Builder(context : Context) {
        internal val swipeBackgroundHolder = SwipeBackgroundHolder(context)
        internal var buttonAction : SwipeButtonAction? = null
        internal var recyclerView : RecyclerView? = null

        internal var firstItemDismissFlag = true
        internal val excludeViewTypeSet = mutableSetOf<Int>()
        internal var isRightToLeft = true
        internal var isFirst : Boolean = true

        fun setSwipeButtonAction(swipeButtonAction : SwipeButtonAction) = this.apply{
            this.buttonAction = swipeButtonAction
        }
        fun setOnRecyclerView(recyclerView : RecyclerView) = this.apply{
            this.recyclerView = recyclerView
        }
        fun setFirstItemSideMarginDp(value : Int) = this.apply{
            swipeBackgroundHolder.firstItemSideMargin = value
        }
        fun setFirstItemDrawable(drawable : Drawable) = this.apply{
            swipeBackgroundHolder.firstIcon = drawable
        }
        fun setBackgroundColor(colorString : String,) = this.apply{
            swipeBackgroundHolder.backgroundColor = Color.parseColor(colorString)
        }
        fun setBackgroundColor(@ColorInt color : Int) = this.apply{
            swipeBackgroundHolder.backgroundColor = color
        }
        fun excludeFromHoldableViewHolder(itemViewType : Int) = this.apply{
            this.excludeViewTypeSet.add(itemViewType)
        }
        fun setDismissOnClickFirstItem(value : Boolean) = this.apply{
            firstItemDismissFlag = value
        }
        fun setDirectionAsRightToLeft(value : Boolean) = this.apply{
            isRightToLeft = value
        }
        fun build() : HoldableSwipeHelper{
            if (buttonAction == null){
                throw IllegalArgumentException("SwipeButtonAction should be implemented. Did you forget to call addSwipedButtonAction()?")
            }
            if (recyclerView == null){
                throw IllegalArgumentException("RecyclerView should be set to HoldableSwipeHandler. Did you forget to call setOnRecyclerView()?")
            }
            return HoldableSwipeHelper(this)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Int {
        return if (viewHolder is ItemHeaderViewHolder){
            makeMovementFlags(0,0)
        } else {
            makeMovementFlags(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (excludeViewTypeSet.contains(viewHolder.itemViewType)){
            return
        }
        absoluteDX = dX
        swipeBackgroundHolder.updateHolderWidth()
        val isHolding = getViewHolderTag(viewHolder)
        scopedX = holdViewPositionHorizontal(dX, isHolding)

        viewHolder.itemView.translationX = scopedX

        swipeBackgroundHolder.drawHoldingBackground(c, viewHolder, scopedX.toInt(), isRightToLeft, isFirst)
        currentViewHolder = viewHolder
    }

    /* 얼마나 길게 swipe해야 인식할 지 */
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        val shouldHold = if (isRightToLeft){
            absoluteDX <= -swipeBackgroundHolder.holderWidth
        } else {
            absoluteDX >= swipeBackgroundHolder.holderWidth
        }
        setViewHolderTag(viewHolder, shouldHold)
        return 2f
    }

    /* 얼마나 빠르게 swipe해야 인식할 지 */
    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 0f
    }
    
    /* item holding을 그만 뒀을 때 원상태로 복구 */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        currentViewHolder?.let{
            if (getViewHolderTag(it)){
                addFirstItemClickListener(recyclerView,viewHolder)
            }
        }
    }
    private fun setViewHolderTag(viewHolder : RecyclerView.ViewHolder, isHolding : Boolean){
        viewHolder.itemView.tag = isHolding
    }
    private fun getViewHolderTag(viewHolder : RecyclerView.ViewHolder?) : Boolean {
        return viewHolder?.itemView?.tag as? Boolean ?: false
    }
    private fun holdViewPositionHorizontal(
        dX : Float,
        isHolding : Boolean
    ) : Float{
        if (isRightToLeft){
            val min : Float = -swipeBackgroundHolder.holderWidth.toFloat()
            val max = 0f
            val x = if(isHolding){
                dX - swipeBackgroundHolder.holderWidth.toFloat()
            } else {
                dX
            }
            return min(max(min,x),max)
        } else {
            val min = 0f
            val max = swipeBackgroundHolder.holderWidth.toFloat()

            val x = if (isHolding){
                dX + swipeBackgroundHolder.holderWidth
            } else {
                dX
            }
            return x.coerceIn(min,max)
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun addFirstItemClickListener(recyclerView : RecyclerView, viewHolder : RecyclerView.ViewHolder){
        recyclerView.setOnTouchListener{ _, downEvent ->
            if (downEvent.action == MotionEvent.ACTION_DOWN){
                if (swipeBackgroundHolder.isFirstItemArea(downEvent.x.toInt(), downEvent.y.toInt())){
                    recyclerView.setOnTouchListener { _, upEvent ->
                        if (upEvent.action == MotionEvent.ACTION_UP){
                            if (swipeBackgroundHolder.isFirstItemArea(upEvent.x.toInt(), upEvent.y.toInt()) && getViewHolderTag(viewHolder)){
                                if (firstItemDismissFlag){
                                    releaseCurrentViewHolderImmediately()
                                }
                                if (viewHolder.adapterPosition >= 0){
                                    buttonAction.onClickFirstButton(viewHolder)
                                    isFirst = !isFirst
                                    //buttonAction.onClickFirstButton(viewHolder.adapterPosition)
                                }
                            }
                        }
                        false
                    }
                }
            }
            return@setOnTouchListener false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun addRecyclerViewListener(recyclerView : RecyclerView){
        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener{
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if (e.action == MotionEvent.ACTION_DOWN){
                    rv.findChildViewUnder(e.x, e.y)?.let{
                        val viewHolder = rv.getChildViewHolder(it)
                        if (viewHolder.adapterPosition != currentViewHolder?.adapterPosition){
                            releaseCurrentViewHolder()
                        }
                    }
                }
                return false
            }
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                releaseCurrentViewHolder()
            }
        })
    }
    private fun addRecyclerViewDecoration(recyclerView : RecyclerView){
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                currentViewHolder?.let{
                    if (getViewHolderTag(it)){
                        swipeBackgroundHolder.run{
                            drawHoldingBackground(c, it, scopedX.toInt(), isRightToLeft, isFirst)
                        }
                    }
                }
            }
        })
    }
    private fun releaseCurrentViewHolderImmediately(){
        currentViewHolder?.apply{
            setViewHolderTag(this, false)
            itemView.translationX = 0f
        }
    }
    private fun releaseCurrentViewHolder(){
        currentViewHolder?.apply{
            setViewHolderTag(this, false)
            itemView.animate().translationX(0f).duration = 300L
            currentViewHolder = null
        }
    }
}