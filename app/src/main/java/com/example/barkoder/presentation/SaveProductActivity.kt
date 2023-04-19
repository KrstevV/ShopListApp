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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolBarBind = ToolBarBinding.inflate(layoutInflater)
        var toolBar = toolBarBind.toolBarr
        setSupportActionBar(toolBar)
        var barcode = intent.getStringExtra("barcodeNumber")
        val currentProduct = ProductDataEntity("",
            barcode, "", "", true,"", "")

        val listProducts = arrayListOf(ProductDataEntity("Marlboro Touch",
            "5319990797575", "Cigari", "140den", true,"", "1")
            ,ProductDataEntity("Kruger",

            "4052700860770", "Sumlivi tableti", "90den", true,"", "1")
            ,ProductDataEntity("Violeta Sredstvo",

            "3870128012806", "Sredstvo za staklo", "115den", true,"", "1"))

                var products = "asdasd"
            if(currentProduct.barcodeProduct == listProducts[0].barcodeProduct) {
                binding.textProductName.text = listProducts[0].nameProduct
                binding.textBarcodeNumber.text = listProducts[0].barcodeProduct
                binding.textProductActive.text = listProducts[0].activeProduct.toString()
                binding.textProductNote.text = listProducts[0].noteProduct
            }
        if(currentProduct.barcodeProduct == listProducts[1].barcodeProduct) {
            binding.textProductName.text = listProducts[1].nameProduct
            binding.textBarcodeNumber.text = listProducts[1].barcodeProduct
            binding.textProductActive.text = listProducts[1].activeProduct.toString()
            binding.textProductNote.text = listProducts[1].noteProduct
        }
        if(currentProduct.barcodeProduct == listProducts[2].barcodeProduct) {
            binding.textProductName.text = listProducts[2].nameProduct
            binding.textBarcodeNumber.text = listProducts[2].barcodeProduct
            binding.textProductActive.text = listProducts[2].activeProduct.toString()
            binding.textProductNote.text = listProducts[2].noteProduct
        }


        if(binding.textBarcodeNumber.text == currentProduct.barcodeProduct) {
        }

        binding.btnSave.setOnClickListener {
            currentProduct.nameProduct = binding.textProductName.text.toString()
            productViewModel.insert(currentProduct)
            var intent = Intent(this@SaveProductActivity, ListProductActivity::class.java)
            startActivity(intent)

        }



    }
}