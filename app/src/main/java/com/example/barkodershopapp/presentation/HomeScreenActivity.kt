package com.example.barkodershopapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.ActivityHomeScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database : FirebaseDatabase
    lateinit var binding : ActivityHomeScreenBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.cont) as NavHostFragment
//            navController = navHostFragment.navController
//
//            findNavController(navHostFragment).navigate(R.id.historyListFragment)

        val fragmentManager = supportFragmentManager
            val targetFragment = HomeScreenFragment()
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.cont, targetFragment)
                transaction.addToBackStack(null)
            transaction.commit()



    }
}