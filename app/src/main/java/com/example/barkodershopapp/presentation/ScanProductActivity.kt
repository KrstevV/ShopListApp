package com.example.barkodershopapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.barkodershopapp.R
import com.example.barkodershopapp.data.listproductdata.ProductData
import com.example.barkodershopapp.databinding.ActivityScanProductBinding
import com.example.barkodershopapp.presentation.viewmodel.ProductViewModel

class ScanProductActivity : AppCompatActivity() {
    lateinit var binding : ActivityScanProductBinding
    lateinit var viewModelProduct : ProductViewModel
    var currentProduct = ProductData("", "", "", "" ,true, "", "")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelProduct = ViewModelProvider(this).get(ProductViewModel::class.java)

        binding.button2.setOnClickListener {
                var result = "asd"
            currentProduct.nameProduct = binding.name.text.toString()
            currentProduct.priceProduct = binding.price.text.toString()
            currentProduct.barcodeProduct = binding.barcode.text.toString()
            currentProduct.count = binding.count.text.toString()
            currentProduct.priceProduct = binding.totalprice.text.toString()

            viewModelProduct.savedProducts(currentProduct)
            var intent = Intent(this@ScanProductActivity, ListProductActivity::class.java)
            startActivity(intent)
            finish()


        }


    }
}