package com.example.barkodershopapp.ui.activities

import android.content.Intent
import android.database.CursorWindow
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.ActivityMainBinding
import com.example.barkodershopapp.ui.viewmodels.ListViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Field


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    val listViewModel: ListViewModel by viewModels()
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        auth = FirebaseAuth.getInstance()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.background = null
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.selectedItemId = R.id.listProductsFragment

    }


//    override fun onBackPressed() {
//        super.onBackPressed()
//        var currentFragment = supportFragmentManager.binding.fragment
//        val intent = Intent(this@MainActivity, HomeScreenActivity::class.java)
//        startActivity(intent)
//        finish()
//    }

    override fun onDestroy() {
        super.onDestroy()
        listViewModel.delete()
    }
}
