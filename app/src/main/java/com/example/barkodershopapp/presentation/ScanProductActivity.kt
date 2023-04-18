package com.example.barkodershopapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.databinding.ActivityScanProductBinding
import com.example.barkodershopapp.presentation.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanProductActivity : AppCompatActivity() {
    lateinit var binding : ActivityScanProductBinding
    val viewModelProduct : ProductViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanProductBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.button2.setOnClickListener {
            var name = binding.name.text.toString()
            var product = ProductDataEntity(name, "" , "", "" , true, "", "",)

            viewModelProduct.insert(product)

            var intent = Intent(this@ScanProductActivity, ListProductActivity::class.java)
            startActivity(intent)
            finish()


        }


    }
}