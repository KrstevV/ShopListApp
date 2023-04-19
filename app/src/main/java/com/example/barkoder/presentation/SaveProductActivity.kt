package com.example.barkoder.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolBarBind = ToolBarBinding.inflate(layoutInflater)
        var toolBar = toolBarBind.toolBarr
        setSupportActionBar(toolBar)
        var barcode = intent.getStringExtra("barcodeNumber")

        val currentProduct = ProductDataEntity("Coca Cola",
            barcode, "", 50, true,"https://www.cokesolutions.com/content/cokesolutions/site/us/en/products/brands/coca-cola/coca-cola.main-image.290-417.png", 1,0)

        val listProducts = arrayListOf(ProductDataEntity("Marlboro Touch",
            "5319990797575", "Cigari", 140, true,"https://verodrive.vero.com.mk/wp-content/uploads/2020/04/3800011820malboro20tac20xl20100.jpg", 1,140)
            ,ProductDataEntity("Kruger",

            "4052700860770", "Sumlivi tableti", 90, true,"https://www.geoprom.com.mk/images/KRUGER/multivitamin.jpg", 1,90)
            ,ProductDataEntity("Violeta Sredstvo",

            "3870128012806", "Sredstvo za staklo", 115, true,"", 1,115))




        Picasso.get().load(currentProduct.imageProduct).into(binding.imageProduct)
        binding.textPrice.text = currentProduct.priceProduct.toString()
        binding.textProductName.text = currentProduct.nameProduct
        binding.textBarcodeNumber.text = currentProduct.barcodeProduct
            if(currentProduct.barcodeProduct == listProducts[0].barcodeProduct) {
                binding.textProductName.text = listProducts[0].nameProduct
                binding.textBarcodeNumber.text = listProducts[0].barcodeProduct
                if(listProducts[0].activeProduct) {
                    binding.textProductActive.text = "Active"
                }
                binding.textPrice.text = listProducts[0].priceProduct.toString()
                binding.textProductNote.text = listProducts[0].noteProduct
                currentProduct.nameProduct = listProducts[0].nameProduct
                currentProduct.imageProduct = listProducts[0].imageProduct
                currentProduct.priceProduct = listProducts[0].priceProduct
                currentProduct.count = listProducts[0].count
                currentProduct.totalPrice = listProducts[0].totalPrice
                Picasso.get().load(listProducts[0].imageProduct).into(binding.imageProduct)
            }
        if(currentProduct.barcodeProduct == listProducts[1].barcodeProduct) {
            binding.textProductName.text = listProducts[1].nameProduct
            binding.textBarcodeNumber.text = listProducts[1].barcodeProduct
            if(listProducts[1].activeProduct) {
                binding.textProductActive.text = "Active"
            }
            binding.textPrice.text = listProducts[1].priceProduct.toString()
            currentProduct.nameProduct = listProducts[1].nameProduct
            currentProduct.imageProduct = listProducts[1].imageProduct
            binding.textProductNote.text = listProducts[1].noteProduct
            currentProduct.priceProduct = listProducts[1].priceProduct
            currentProduct.count = listProducts[1].count
            currentProduct.totalPrice = listProducts[1].totalPrice
            Picasso.get().load(listProducts[1].imageProduct).into(binding.imageProduct)
        }
        if(currentProduct.barcodeProduct == listProducts[2].barcodeProduct) {
            binding.textProductName.text = listProducts[2].nameProduct
            binding.textBarcodeNumber.text = listProducts[2].barcodeProduct
            if(listProducts[2].activeProduct) {
                binding.textProductActive.text = "Active"
            }
            binding.textProductNote.text = listProducts[2].noteProduct
        }

        binding.btnSave.setOnClickListener {


                productViewModel.insert(currentProduct)
                var intent = Intent(this@SaveProductActivity, ListProductActivity::class.java)
                startActivity(intent)
        }
    }
}