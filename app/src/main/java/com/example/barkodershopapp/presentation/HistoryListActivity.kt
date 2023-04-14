package com.example.barkodershopapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.barkodershopapp.data.listhistorydata.HistoryListData
import com.example.barkodershopapp.databinding.ActivityHistoryListBinding
import com.example.barkodershopapp.databinding.ToolBarBinding
import com.example.barkodershopapp.presentation.Adapters.HistoryAdapter
import com.example.barkodershopapp.presentation.viewmodel.HistoryViewModel

class HistoryListActivity : AppCompatActivity() {
    private lateinit var toolBarBind : ToolBarBinding
    lateinit var binding : ActivityHistoryListBinding
    lateinit var historyViewModel : HistoryViewModel
    lateinit var historyAdapter : HistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        toolBarBind = ToolBarBinding.inflate(layoutInflater)
        var toolBar = toolBarBind.toolBarr
        setSupportActionBar(toolBar)
            var listH = listOf<HistoryListData>()
        historyAdapter = HistoryAdapter(listH)
       historyViewModel.getHistoryLists()
        binding.recViewHistoryList.apply {
            layoutManager = LinearLayoutManager(this@HistoryListActivity)
            adapter = historyAdapter

        }
        historyViewModel.listsHistory.observe(this, Observer {
            historyAdapter.setNotesList(it)
        })


        binding.btnNewList.setOnClickListener {
            var intent = Intent(this@HistoryListActivity, ListProductActivity::class.java)
            startActivity(intent)
        }

    }
}