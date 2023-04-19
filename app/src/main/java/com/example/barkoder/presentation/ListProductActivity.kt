package com.example.barkoder.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barkoder.shoppingApp.net.databinding.ActivityListProductBinding
import com.barkoder.shoppingApp.net.databinding.ToolBarBinding
import com.example.barkoder.data.listhistorydata.swipecallback.SwipeToDelete
import com.example.barkoder.data.room.HistoryDataEntity
import com.example.barkoder.data.room.ProductDataEntity
import com.example.barkoder.presentation.Adapters.ProductAdapter
import com.example.barkoder.presentation.viewmodel.HistoryViewModel
import com.example.barkoder.presentation.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ListProductActivity : AppCompatActivity() {
    private lateinit var toolBarBind : ToolBarBinding
    lateinit var productAdatper : ProductAdapter
    lateinit var binding : ActivityListProductBinding
    val productViewModel : ProductViewModel by viewModels()
    val historyViewModel : HistoryViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var productL = listOf<ProductDataEntity>()
        productAdatper = ProductAdapter(productL)

        toolBarBind = ToolBarBinding.inflate(layoutInflater)
        var toolBar = toolBarBind.toolBarr
        setSupportActionBar(toolBar)

        binding.recViewProductList.apply {
            layoutManager = LinearLayoutManager(this@ListProductActivity)
            adapter = productAdatper
        }

        productViewModel.allNotes.observe(this, {
            productAdatper.setNotesList(it)
        })


        binding.btnScan.setOnClickListener {
            var intent = Intent(this@ListProductActivity, ScanProductActivity::class.java)
            startActivity(intent)
        }

        val itemTouchHelper = ItemTouchHelper(swipteToDelete)

        itemTouchHelper.attachToRecyclerView(binding.recViewProductList)


        binding.btnAddList.setOnClickListener {
                if(binding.editTextListName.text.toString() != "") {
                    var name  = binding.editTextListName.text.toString()
                    var date = "Created at ${getCurrentDate()}"
                    val currentHistory = HistoryDataEntity(name, date)
                    historyViewModel.insert(currentHistory)
                    var intent = Intent(this@ListProductActivity, HistoryListActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        }



    }

   private val swipteToDelete = object : SwipeToDelete() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.absoluteAdapterPosition
            productAdatper.notifyItemRemoved(position)
            productViewModel.deleteItem(productAdatper.getProductInt(position))

        }
    }
    private fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return formatter.format(currentDate)


    }

}