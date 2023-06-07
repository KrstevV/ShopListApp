package com.example.barkodershopapp.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.barkoder.Barkoder
import com.barkoder.BarkoderConfig
import com.barkoder.interfaces.BarkoderResultCallback
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentScanThreeBinding
import com.barkoder.shoppingApp.net.databinding.FragmentScanTwoBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanThreeFragment : Fragment(), BarkoderResultCallback {
    lateinit var binding : FragmentScanThreeBinding
    private val CAMERA_PERMISSION_REQUEST_CODE = 200


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentScanThreeBinding.inflate(inflater, container, false)
        binding.bkdView3.config = BarkoderConfig(
            context,
            "PEmBIohr9EZXgCkySoetbwP4gvOfMcGzgxKPL2X6uqPEh7C-NSQGuK_IHt6EYbMPXg2o0WbjAzGF9mRZeL-hAMzUHLYRmxeuHlH3yXiPf0ET7RUMN4HS_-xvZkoYsrgP8Eus3e9OaFTV-SkKu-c6g1mwZwMYHHTd9mfp1u9bAzqQlJgk_3xSb3_GFCqnDOUkPW_a9KTXtobdEbTXFI3b_tTWATSfBgIfeO-uzbhyI8xUT4xTDLU6GaIsXzHenpljgw3LoYqmIs86nLfx1zrtXvANu-YhYC1GowX2WPMJXVI."
        )
        {
            Log.i("LicenseInfo", it.message)
            Log.i("viktor", Barkoder.GetVersion())
        }

        if (checkCameraPermission()) {
            startScanning()
            navInvisible()
        } else {
            requestCameraPermission()
            findNavController().popBackStack()
        }


        setActiveBarcodeTypes()
        setBarkoderSettings()
        navInvisible()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun navInvisible(){
        var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
        bottomNav.visibility = View.GONE
        var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        bottomFab.visibility = View.GONE
    }
    private fun setActiveBarcodeTypes() {
        // There is option to set multiple active barcodes at once as array
        binding.bkdView3.config.decoderConfig.SetEnabledDecoders(
            arrayOf(
                Barkoder.DecoderType.QR,
                Barkoder.DecoderType.Ean13
            )
        )
        // or configure them one by one
        binding.bkdView3.config.decoderConfig.UpcA.enabled = true
    }

    private fun setBarkoderSettings() {
        // These are optional settings, otherwise default values will be used
        binding.bkdView3.config.let { config ->
            config.isImageResultEnabled = true
            config.isLocationInImageResultEnabled = true
            config.isRegionOfInterestVisible = true
            config.isPinchToZoomEnabled = true
            config.setRegionOfInterest(5f, 5f, 90f, 90f)
        }
    }

    private fun updateUI(result: Barkoder.Result? = null, resultImage: Bitmap? = null) {
        var barcodeNum3 = result?.textualData

        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            "barcodeNum3",
            barcodeNum3
        )
        findNavController().popBackStack()
    }

    override fun scanningFinished(results: Array<Barkoder.Result>, resultImage: Bitmap?) {
        if (results.isNotEmpty())
            updateUI(results[0], resultImage)
        else
            updateUI()
    }

    private fun startScanning() {

        binding.bkdView3.startScanning(this)

    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA,
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startScanning()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Camera permission denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
        bottomNav.visibility = View.VISIBLE
        var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        bottomFab.visibility = View.VISIBLE
    }


}