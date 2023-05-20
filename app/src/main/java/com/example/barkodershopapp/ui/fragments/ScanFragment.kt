package com.example.barkodershopapp.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import coil.load
import com.barkoder.Barkoder
import com.barkoder.Barkoder.LicenseCheckListener
import com.barkoder.Barkoder.LicenseCheckResult
import com.barkoder.BarkoderConfig
import com.barkoder.interfaces.BarkoderResultCallback
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentScanBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanFragment : Fragment(), BarkoderResultCallback{
    lateinit var binding: FragmentScanBinding
    private lateinit var callback: OnBackPressedCallback
    private val cameraRequest = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentScanBinding.inflate(inflater, container, false)
        var btnScan = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        btnScan.isClickable = false
        binding.bkdView.config = BarkoderConfig(
            context,
            "PEmBIohr9EZXgCkySoetbwP4gvOfMcGzgxKPL2X6uqPEh7C-NSQGuK_IHt6EYbMPXg2o0WbjAzGF9mRZeL-hAMzUHLYRmxeuHlH3yXiPf0ET7RUMN4HS_-xvZkoYsrgP8Eus3e9OaFTV-SkKu-c6g1mwZwMYHHTd9mfp1u9bAzqQlJgk_3xSb3_GFCqnDOUkPW_a9KTXtobdEbTXFI3b_tTWATSfBgIfeO-uzbhyI8xUT4xTDLU6GaIsXzHenpljgw3LoYqmIs86nLfx1zrtXvANu-YhYC1GowX2WPMJXVI."
        )
        {
            Log.i("LicenseInfo", it.message)
        }
        binding.bkdView.startScanning(this)

        setActiveBarcodeTypes()
        setBarkoderSettings()
        onBackButton()

        return binding.root
    }

    private fun setActiveBarcodeTypes() {
        // There is option to set multiple active barcodes at once as array
        binding.bkdView.config.decoderConfig.SetEnabledDecoders(
            arrayOf(
                Barkoder.DecoderType.QR,
                Barkoder.DecoderType.Ean13
            )
        )
        // or configure them one by one
        binding.bkdView.config.decoderConfig.UpcA.enabled = true
    }

    private fun setBarkoderSettings() {
        // These are optional settings, otherwise default values will be used
        binding.bkdView.config.let { config ->
            config.isImageResultEnabled = true
            config.isLocationInImageResultEnabled = true
            config.isRegionOfInterestVisible = true
            config.isPinchToZoomEnabled = true
            config.setRegionOfInterest(5f, 5f, 90f, 90f)
        }
    }

    private fun updateUI(result: Barkoder.Result? = null, resultImage: Bitmap? = null) {
        var barcodeNum = result?.textualData
        binding.textView7.text = barcodeNum
        val bundle = Bundle()
        bundle.putString("barcodeNum", barcodeNum)
        findNavController().navigate(
            R.id.addProductFragment,
            bundle,

        )
    }

    override fun scanningFinished(results: Array<Barkoder.Result>, resultImage: Bitmap?) {
        if (results.isNotEmpty())
            updateUI(results[0], resultImage)
        else
            updateUI()
    }

    private fun onBackButton(){
        callback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {

                findNavController().navigate(
                    R.id.homeScreenFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.scanFragment, true).build()
                )

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }




}