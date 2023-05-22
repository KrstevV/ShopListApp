package com.example.barkodershopapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.barkoder.shoppingApp.net.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
    }
}