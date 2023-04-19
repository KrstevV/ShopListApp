package com.example.barkoder.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.barkoder.shoppingApp.net.databinding.ActivitySaveProductBinding
import com.barkoder.shoppingApp.net.databinding.ToolBarBinding
import com.example.barkoder.data.room.ProductDataEntity
import com.example.barkoder.presentation.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveProductActivity : AppCompatActivity() {
    lateinit var binding  : ActivitySaveProductBinding
    private lateinit var toolBarBind : ToolBarBinding
    val productViewModel : ProductViewModel by viewModels()
    val currentProduct = ProductDataEntity("",
        "", "", "", true,"", "")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolBarBind = ToolBarBinding.inflate(layoutInflater)
        var toolBar = toolBarBind.toolBarr
        setSupportActionBar(toolBar)

        binding.btnSave.setOnClickListener {
            currentProduct.nameProduct = binding.textProductName.text.toString()
            currentProduct.barcodeProduct = binding.textBarcodeNumber.text.toString()
            productViewModel.insert(currentProduct)
            var intent = Intent(this@SaveProductActivity, ListProductActivity::class.java)
            startActivity(intent)

        }



    }
}