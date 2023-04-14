package com.example.barkodershopapp.presentation

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.example.barkodershopapp.R
import com.example.barkodershopapp.databinding.ActivitySaveProductBinding
import com.example.barkodershopapp.databinding.ToolBarBinding

class SaveProductActivity : AppCompatActivity() {
    lateinit var binding  : ActivitySaveProductBinding
    private lateinit var toolBarBind : ToolBarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolBarBind = ToolBarBinding.inflate(layoutInflater)
        var toolBar = toolBarBind.toolBarr
        setSupportActionBar(toolBar)

        binding.btnSave.setOnClickListener {
            var intent = Intent(this@SaveProductActivity, ListProductActivity::class.java)
            startActivity(intent)
            finish()
        }



    }
}