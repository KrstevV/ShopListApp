package com.example.barkodershopapp.presentation

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.barkoder.Barkoder
import com.barkoder.BarkoderConfig
import com.barkoder.BarkoderHelper
import com.barkoder.enums.BarkoderConfigTemplate
import com.barkoder.interfaces.BarkoderResultCallback
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentScanBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanFragment : Fragment(), BarkoderResultCallback {
        lateinit var binding : FragmentScanBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentScanBinding.inflate(inflater, container, false)

        binding.bkdView.config = BarkoderConfig(context, "PEmBIohr9EZXgCkySoetbwP4gvOfMcGzgxKPL2X6uqPEh7C-NSQGuK_IHt6EYbMPzeLT1AQCKl8pkQkYm47d552Ox0VqVPdVROBDs0NDXebSB7D9bUsI_IJPZsrx-Hmuc-xfH8hokLbr4tmXeorlavEmLZJqBb1s3Z5Uuve8paQldQev5o7JbAEYPJj_Wgce8ftwiyAlUmU9vKt2RJTHIpmshcFNDBo3HLSsmchCI8ciT58nntrTWoYkApGly4w2")
        { Log.i("LicenseInfo", it.message)
        }
        BarkoderHelper.applyConfigSettingsFromTemplate( context,
            binding.bkdView.config, BarkoderConfigTemplate.INDUSTRIAL_1D, null
        )
        binding.bkdView.startScanning(this)

        setActiveBarcodeTypes()
        setBarkoderSettings()




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



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
        binding.textView6.text = barcodeNum
        val bundle = Bundle()
        bundle.putString("barcodeNum", barcodeNum)


        findNavController().navigate(R.id.action_scanFragment_to_addProductFragment, bundle)

    }
    override fun scanningFinished(results: Array<Barkoder.Result>, resultImage: Bitmap?) {

        if (results.isNotEmpty())
            updateUI(results[0], resultImage)
        else
            updateUI()
    }

}