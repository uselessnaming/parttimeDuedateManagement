package com.example.parttimeduedatemanagement.Event

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.parttimeduedatemanagement.Adapater.ItemChildViewHolder
import java.lang.Math.abs

class SwipeController(val context : Context) : ItemTouchHelper.Callback() {

    private var swipeStarted = false
    private var initialSwipeX = 0f
    companion object{
        private val MAX_SWIPE_DISTANCE = 200f
        private val SWIPE_THRESHOLD = 0f
    }
    override fun getMovementFlags(recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder,): Int {
        val swipeFlags = ItemTouchHelper.RIGHT
        return makeMovementFlags(0,swipeFlags)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val itemAdapter = viewHolder as ItemChildViewHolder
        if (direction == ItemTouchHelper.RIGHT) {

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
            if (!swipeStarted && abs(dX) < SWIPE_THRESHOLD){
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
                val view = (viewHolder as ItemChildViewHolder).layout
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