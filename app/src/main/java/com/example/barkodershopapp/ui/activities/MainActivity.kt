package com.example.barkodershopapp.ui.activities

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.database.CursorWindow
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.ActivityMainBinding
import com.example.barkodershopapp.ui.viewmodels.ListViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Field
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var isEditMode: Boolean = false
    val listViewModel: ListViewModel by viewModels()
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private var currentFragment:    Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout

        val toolBar = binding.toolBarrr
        setSupportActionBar(toolBar)

        val navigationView = binding.drawerView
        navigationView.setNavigationItemSelectedListener(this)

        val toogle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolBar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        setDefaultLanguage()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.background = null
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val selectedLanguage = sharedPreferences.getString(PREF_SELECTED_LANGUAGE, "en-us")

        if(selectedLanguage == "en-us") {
            setDefaultLanguage()
        } else {
            setMKLanguage()
        }

        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.selectedItemId = R.id.homeScreenFragment

    }

    companion object {
        private const val PREF_SELECTED_LANGUAGE = "selected_language"
    }


    private fun setDefaultLanguage() {
        val locale = Locale("en-us")
        Locale.setDefault(locale)

        val resources = resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

    }

    private fun setMKLanguage() {
        val locale = Locale("mk")
        Locale.setDefault(locale)

        val resources = resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

    }





    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    fun setIsEditMode(value: Boolean) {
        isEditMode = value
    }

    fun getIsEditMode(): Boolean {
        return isEditMode
    }


    override fun onDestroy() {
        super.onDestroy()
        listViewModel.delete()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.drawerHome -> navController.navigate(R.id.homeScreenFragment)
            R.id.drawerAbout -> navController.navigate(R.id.addProductFragment)
            R.id.drawerSettings -> navController.navigate(R.id.settingsFragment)
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else {
            onBackPressedDispatcher.onBackPressed()
        }
    }


}
