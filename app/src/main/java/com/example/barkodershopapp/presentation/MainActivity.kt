package com.example.barkodershopapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.barkoder.shoppingApp.net.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth

    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.singOutText.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val i  = Intent(this, LoginActivity::class.java)
            startActivity(i)

        }

        binding.button.setOnClickListener {
            var intent = Intent(this@MainActivity, SaveProductActivity::class.java)
            startActivity(intent)
            finish()
        }




    }
}