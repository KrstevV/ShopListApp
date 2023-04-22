package com.example.barkoder.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.barkoder.shoppingApp.net.databinding.ActivityAddProductBinding
import com.example.barkoder.data.room.ProductDataEntity
import com.example.barkoder.presentation.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProduct : AppCompatActivity() {
    lateinit var binding : ActivityAddProductBinding
    val productViewModel : ProductViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var currentAddProduct = ProductDataEntity("productName","productBarcode","productNotes", 0, true, "https://st4.depositphotos.com/14953852/24787/v/600/depositphotos_247872612-stock-illustration-no-image-available-icon-vector.jpg", 1, 0)

        var barcode2 = intent.getStringExtra("barcodeNumber2")

        binding.editTextBarcodeAddProduct.setText(barcode2)

        binding.btnScanAddProduct.setOnClickListener {
            var intent = Intent(this@AddProduct, ScanAddProductActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.btnAddProductToList.setOnClickListener {

            var productName = binding.editTextNameAddProduct.text.toString()
            var productBarcode = binding.editTextBarcodeAddProduct.text.toString()
            var productPrice = binding.editPriceAddProduct.text.toString()
            var productNotes = binding.editTextNotesAddProduct.text.toString()

            if(productName.isNotEmpty() && productNotes.isNotEmpty() && productPrice.isNotEmpty()) {
                if(binding.checkBox.isChecked) {
                    currentAddProduct.activeProduct = true
                } else {
                    currentAddProduct.activeProduct = false
                }
                currentAddProduct.nameProduct = productName
                currentAddProduct.barcodeProduct = productBarcode
                currentAddProduct.priceProduct = productPrice.toInt()
                currentAddProduct.noteProduct = productNotes
                currentAddProduct.totalPrice = productPrice.toInt()

                productViewModel.insert(currentAddProduct)


                var intent = Intent(this@AddProduct, ListProductActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }


        }
    }

}