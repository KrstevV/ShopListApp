package com.example.barkoder.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.ActivitySaveProductBinding
import com.barkoder.shoppingApp.net.databinding.ToolBarBinding
import com.example.barkoder.data.room.ProductDataEntity
import com.example.barkoder.presentation.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveProductActivity : AppCompatActivity() {
    lateinit var binding  : ActivitySaveProductBinding
    private lateinit var toolBarBind : ToolBarBinding
    val productViewModel : ProductViewModel by viewModels()

    val currentProduct = ProductDataEntity("Coca Cola",
        "", "", 50, true,"https://www.cokesolutions.com/content/cokesolutions/site/us/en/products/brands/coca-cola/coca-cola.main-image.290-417.png", 1,0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolBarBind = ToolBarBinding.inflate(layoutInflater)
        var toolBar = toolBarBind.toolBarr
        setSupportActionBar(toolBar)
        var barcode = intent.getStringExtra("barcodeNumber")
        currentProduct.barcodeProduct = barcode


        val listProducts = arrayListOf(ProductDataEntity("Marlboro Touch",
            "5319990797575", "Cigari", 140, true,"https://verodrive.vero.com.mk/wp-content/uploads/2020/04/3800011820malboro20tac20xl20100.jpg", 1,140)
            ,ProductDataEntity("Kruger",

            "4052700860770", "Sumlivi tableti", 90, true,"https://www.geoprom.com.mk/images/KRUGER/multivitamin.jpg", 1,90)
            ,ProductDataEntity("Violeta Sredstvo",

            "3870128012806", "Sredstvo za staklo", 115, true,"", 1,115))

        forLoopProduct(listProducts)
        binding.textPrice.text = currentProduct.priceProduct.toString()
        binding.textProductName.text = currentProduct.nameProduct
        binding.textBarcodeNumber.text = currentProduct.barcodeProduct
        binding.textPrice.text = currentProduct.priceProduct.toString()
        var cupo = currentProduct.imageProduct

        if (currentProduct.imageProduct!!.isEmpty()) {
            binding.imageProduct.setImageResource(R.drawable.ic_broken_image);
        } else{
            Picasso.get().load(cupo).into(binding.imageProduct);
        }


        binding.btnSave.setOnClickListener {

                productViewModel.insert(currentProduct)
                var intent = Intent(this@SaveProductActivity, ListProductActivity::class.java)
                startActivity(intent)
        }
    }
    private fun forLoopProduct(list : ArrayList<ProductDataEntity>) {
        for(i in list) {
            if(currentProduct.barcodeProduct == i.barcodeProduct) {
                currentProduct.nameProduct = i.nameProduct
                currentProduct.imageProduct = i.imageProduct
                currentProduct.priceProduct = i.priceProduct
                currentProduct.totalPrice = i.totalPrice
                currentProduct.activeProduct = i.activeProduct
                currentProduct.count = i.count
            }
        }

        }


    }

