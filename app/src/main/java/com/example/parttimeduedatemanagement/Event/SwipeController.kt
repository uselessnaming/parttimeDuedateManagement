package com.example.parttimeduedatemanagement.Event

import android.content.Context
import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.parttimeduedatemanagement.Adapater.ItemChildViewHolder
import java.lang.Math.abs

class SwipeController(val context : Context) : ItemTouchHelper.Callback() {

    private var swipeStarted = false
    private var initialSwipeX = 0f
    companion object{
        private const val MAX_SWIPE_DISTANCE = 250f
        private const val SWIPE_THRESHOLD = 0f
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * 10
    }
    override fun getMovementFlags(recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder,): Int {
        val swipeFlags = ItemTouchHelper.RIGHT
        return makeMovementFlags(0,swipeFlags)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val itemAdapter = viewHolder as ItemChildViewHolder
        if (direction == ItemTouchHelper.RIGHT) {
            itemAdapter.binding.imgCancel.setOnClickListener{
                itemAdapter.binding.imgCancel.visibility = View.GONE
                itemAdapter.binding.imgSoldOut.visibility = View.VISIBLE
            }
            itemAdapter.binding.imgSoldOut.setOnClickListener{
                itemAdapter.binding.imgSoldOut.visibility = View.GONE
                itemAdapter.binding.imgCancel.visibility = View.VISIBLE
            }
        }
    }

    override fun onMove(recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder,target: RecyclerView.ViewHolder,): Boolean {
        return false
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
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            if (!swipeStarted && kotlin.math.abs(dX) < SWIPE_THRESHOLD){
                return super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            } else {
                swipeStarted = true
                if (initialSwipeX == 0f){
                    initialSwipeX = dX
                }
                val newDX = if (abs(dX - initialSwipeX) > MAX_SWIPE_DISTANCE){
                    if (dX > 0) MAX_SWIPE_DISTANCE else -MAX_SWIPE_DISTANCE
                } else {
                    dX - initialSwipeX
                }
                val view = (viewHolder as ItemChildViewHolder).binding.layoutMain
                getDefaultUIUtil().onDraw(c,recyclerView,view,newDX,dY,actionState,isCurrentlyActive)
            }
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder as ItemChildViewHolder)
        swipeStarted = false
        initialSwipeX = 0f
    }
}