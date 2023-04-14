package com.example.barkodershopapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.barkodershopapp.data.listhistorydata.HistoryListData
import com.example.barkodershopapp.data.listproductdata.ProductData
import com.example.barkodershopapp.databinding.ActivityListProductBinding
import com.example.barkodershopapp.databinding.ToolBarBinding
import com.example.barkodershopapp.presentation.Adapters.ProductAdapter
import com.example.barkodershopapp.presentation.viewmodel.HistoryViewModel
import com.example.barkodershopapp.presentation.viewmodel.ProductViewModel
import java.text.SimpleDateFormat
import java.util.*

class ListProductActivity : AppCompatActivity() {
    private lateinit var toolBarBind : ToolBarBinding
    private lateinit var historyViewModel : HistoryViewModel
    private lateinit var productViewModel : ProductViewModel
    lateinit var productAdatper : ProductAdapter
    private var currentHistory = HistoryListData("", "")
    lateinit var binding : ActivityListProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var productL = listOf<ProductData>()
        productAdatper = ProductAdapter(productL)

        toolBarBind = ToolBarBinding.inflate(layoutInflater)
        var toolBar = toolBarBind.toolBarr
        setSupportActionBar(toolBar)
        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        productViewModel.getProductsLists()



        binding.recViewProductList.apply {
            layoutManager = LinearLayoutManager(this@ListProductActivity)
            adapter = productAdatper
        }

        productViewModel.listsProducts.observe(this,  {
             productAdatper.setNotesList(it)
        })

        binding.btnScan.setOnClickListener {
            var intent = Intent(this@ListProductActivity, ScanProductActivity::class.java)
            startActivity(intent)
        }


        binding.btnAddList.setOnClickListener {
                if(binding.editTextListName.text.toString() != "") {
                    currentHistory.nameList = binding.editTextListName.text.toString()
                    currentHistory.createdListDate = "Created at ${getCurrentDate()}"
                    historyViewModel.savedHistory(currentHistory)
                    var intent = Intent(this@ListProductActivity, HistoryListActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        }

    }

    private fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return formatter.format(currentDate)


    }
}