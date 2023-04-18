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
    val productViewModel : ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanProductBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.button2.setOnClickListener {
            var intentT = Intent(this@ScanProductActivity, SaveProductActivity::class.java)
            startActivity(intentT)
            finish()

        }


    }
}