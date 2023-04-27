package com.example.barkodershopapp.presentation

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.ActivitySaveProductBinding
import com.barkoder.shoppingApp.net.databinding.ToolBarBinding
import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.domain.userdataacc.Product
import com.example.barkodershopapp.presentation.viewmodel.ProductApiViewModel
import com.example.barkodershopapp.presentation.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveProductActivity : AppCompatActivity() {
    lateinit var binding  : ActivitySaveProductBinding
    private lateinit var toolBarBind : ToolBarBinding
    val productViewModel : ProductViewModel by viewModels()
    val productApiViewModel : ProductApiViewModel by viewModels()


    var currentProduct = ProductDataEntity("",
        "", "", 0, true,null, 1,0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolBarBind = ToolBarBinding.inflate(layoutInflater)
        var toolBar = toolBarBind.toolBarr
        setSupportActionBar(toolBar)
        var barcode = intent.getStringExtra("barcodeNumber")
        currentProduct.barcodeProduct = barcode
        productApiViewModel.getAllProductsApi()
        var products = productApiViewModel.resp

        productApiViewModel.resp.observe(this, {
            forLoopProduct(it.products as ArrayList<Product>)
            binding.textPrice.text = currentProduct.priceProduct.toString()
            binding.textProductName.text = currentProduct.nameProduct
            binding.textBarcodeNumber.text = currentProduct.barcodeProduct
            binding.textPrice.text = currentProduct.priceProduct.toString()
            binding.textProductNote.text = currentProduct.noteProduct
            var cupo = currentProduct.imageProduct

//            if (currentProduct.imageProduct!!.isEmpty()) {
//                binding.imageProduct.setImageResource(R.drawable.ic_broken_image)
//                binding.btnSave.isClickable = false
//                binding.btnSave.setBackgroundColor(Color.GRAY)
//            } else{
//                Picasso.get().load(cupo).into(binding.imageProduct);
//            }

        })



        binding.btnSave.setOnClickListener {

                productViewModel.insert(currentProduct)
//                var intent = Intent(this@SaveProductActivity, ListProductActivity::class.java)
//                startActivity(intent)
//                 finish()
        }

        binding.btnCancel.setOnClickListener {
//                var intent = Intent(this@SaveProductActivity, ListProductActivity::class.java)
//                startActivity(intent)
//            finish()
        }
    }

    private fun forLoopProduct(list : ArrayList<Product>) {
        for(i in list) {
            if(currentProduct.barcodeProduct == i.barcode) {
                currentProduct.nameProduct = i.name
                currentProduct.noteProduct = i.notes
//                currentProduct.imageProduct = i.image
                currentProduct.priceProduct = i.price
                currentProduct.totalPrice = i.totalprice
//                currentProduct.activeProduct = i
                currentProduct.count = i.count
            }
        }

        }


    }

