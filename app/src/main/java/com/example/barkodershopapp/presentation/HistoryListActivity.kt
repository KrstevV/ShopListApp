package com.example.barkodershopapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barkodershopapp.data.listhistorydata.HistoryListData
import com.example.barkodershopapp.data.listhistorydata.swipecallback.SwipeToDelete
import com.example.barkodershopapp.data.listhistorydata.swipecallback.onSwipeListener
import com.example.barkodershopapp.data.room.HistoryDataEntity
import com.example.barkodershopapp.databinding.ActivityHistoryListBinding
import com.example.barkodershopapp.databinding.ToolBarBinding
import com.example.barkodershopapp.presentation.Adapters.HistoryAdapter
import com.example.barkodershopapp.presentation.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryListActivity : AppCompatActivity() {
    private lateinit var toolBarBind: ToolBarBinding
    lateinit var binding: ActivityHistoryListBinding
    val historyViewModel: HistoryViewModel by viewModels()
    lateinit var historyAdapter: HistoryAdapter
    var listH = arrayListOf<HistoryDataEntity>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolBarBind = ToolBarBinding.inflate(layoutInflater)
        var toolBar = toolBarBind.toolBarr
        setSupportActionBar(toolBar)


        historyAdapter = HistoryAdapter(listH)

        binding.recViewHistoryList.apply {
            layoutManager = LinearLayoutManager(this@HistoryListActivity)
            adapter = historyAdapter

        }
        historyViewModel.allNotes.observe(this, Observer {
            historyAdapter.setNotesList(it as ArrayList<HistoryDataEntity>)

        })

        val itemTouchHelper = ItemTouchHelper(swipteToDelete)

        itemTouchHelper.attachToRecyclerView(binding.recViewHistoryList)


        binding.btnNewList.setOnClickListener {
            var intent = Intent(this@HistoryListActivity, ListProductActivity::class.java)
            startActivity(intent)
        }
    }

        val swipteToDelete = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                historyAdapter.notifyItemRemoved(position)
                historyViewModel.deleteItem(historyAdapter.getHistoryInt(position))

            }
    }
    var swipteListener = object : onSwipeListener {

        override fun swipteLeftToDelete(list: HistoryDataEntity) {
            historyViewModel.deleteItem(list)
            swipteToDelete
        }
    }



}





