package com.example.barkoder.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.ActivityHomeScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenActivity : AppCompatActivity() {
    lateinit var binding : ActivityHomeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnShopNow.setOnClickListener {
            var intent = Intent(this@HomeScreenActivity, ListProductActivity::class.java)
            startActivity(intent)
        }

        binding.btnShopHistory.setOnClickListener {
            var intent = Intent(this@HomeScreenActivity, HistoryListActivity::class.java)
            startActivity(intent)
        }


    }
}