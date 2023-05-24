package com.example.barkodershopapp.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentSettingsBinding
import com.example.barkodershopapp.ui.activities.MainActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class SettingsFragment : Fragment() {
    lateinit var binding : FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentSettingsBinding.inflate(inflater, container, false)
        requireActivity().title = "Settings"
        navInvisible()




        return  binding.root
    }

    companion object {
        private const val PREF_SELECTED_LANGUAGE = "selected_language"
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val selectedLanguage = sharedPreferences.getString(PREF_SELECTED_LANGUAGE, "en-us")



        when (selectedLanguage) {
            "en-us" -> binding.radioEnglish.isChecked = true
            "mk" -> binding.radioMacedonia.isChecked = true
        }


        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val newLanguage = when (checkedId) {
                R.id.radioEnglish -> "en-us"
                R.id.radioMacedonia -> "mk"
                else -> "en-us"
            }


            val editor = sharedPreferences.edit()
            editor.putString(PREF_SELECTED_LANGUAGE, newLanguage)
            editor.apply()

            setLocale(Locale(newLanguage))
        }

    }

    private fun setLocale(locale: Locale) {
        val resources = requireActivity().resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

    }




    private fun navInvisible(){
        var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
        bottomNav.visibility = View.GONE
        var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        bottomFab.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()





        var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
        bottomNav.visibility = View.VISIBLE
        var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        bottomFab.visibility = View.VISIBLE
    }



    override fun onResume() {
        super.onResume()





    }
}



