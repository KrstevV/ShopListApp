package com.example.barkodershopapp.data.listhistorydata.swipecallback

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.barkodershopapp.data.listhistorydata.HistoryListData

abstract class SwipeToDelete : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipteFlag = ItemTouchHelper.LEFT
        return makeMovementFlags(0, swipteFlag)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

}