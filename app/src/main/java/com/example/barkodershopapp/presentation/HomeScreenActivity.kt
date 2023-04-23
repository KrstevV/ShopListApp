package com.example.barkodershopapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.barkoder.shoppingApp.net.databinding.ActivityHomeScreenBinding
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenActivity : AppCompatActivity() {
    private lateinit var database : FirebaseDatabase
    lateinit var binding : ActivityHomeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()

        var dataReg = database.reference.child("nemaveza").child("sdfs").child("sfds")
        dataReg.setValue("viktor")

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