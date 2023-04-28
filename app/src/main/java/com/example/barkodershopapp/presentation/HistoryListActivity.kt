package com.example.barkodershopapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.ActivityHistoryListBinding
import com.barkoder.shoppingApp.net.databinding.ToolBarBinding
import com.example.barkodershopapp.data.listhistorydata.swipecallback.SwipeToDelete
import com.example.barkodershopapp.data.room.HistoryDataEntity
import com.example.barkodershopapp.presentation.Adapters.HistoryAdapter
import com.example.barkodershopapp.presentation.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryListActivity : AppCompatActivity() {
    private lateinit var toolBarBind: ToolBarBinding
    lateinit var binding: ActivityHistoryListBinding
    var listH = arrayListOf<HistoryDataEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolBarBind = ToolBarBinding.inflate(layoutInflater)
        var toolBar = toolBarBind.toolBarr
        setSupportActionBar(toolBar)



//        binding.btnNewList.setOnClickListener {
//                        val fragmentManager = supportFragmentManager
//            val targetFragment = HistoryListFragment()
//            val transaction = fragmentManager.beginTransaction()
//            transaction.replace(binding.hLayout, targetFragment)
//                transaction.addToBackStack(null)
//            transaction.commit()
//        }
    }


      /*  private val swipteToDelete = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                historyAdapter.notifyItemRemoved(position)
                historyViewModel.deleteItem(historyAdapter.getHistoryInt(position))

            }
    }*/

    private val onClick = object  : OnClickListener {

        override fun onClick(list: HistoryDataEntity) {

        }

    }




}





