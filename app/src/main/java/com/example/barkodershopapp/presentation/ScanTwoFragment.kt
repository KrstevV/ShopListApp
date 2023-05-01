package com.example.barkodershopapp.presentation

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.barkoder.Barkoder
import com.barkoder.BarkoderConfig
import com.barkoder.BarkoderHelper
import com.barkoder.enums.BarkoderConfigTemplate
import com.barkoder.interfaces.BarkoderResultCallback
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentScanTwoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanTwoFragment : Fragment(), BarkoderResultCallback {
    lateinit var binding: FragmentScanTwoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScanTwoBinding.inflate(inflater, container, false)


        binding.bkdView2.config = BarkoderConfig(
            context,
            "PEmBIohr9EZXgCkySoetbwP4gvOfMcGzgxKPL2X6uqPEh7C-NSQGuK_IHt6EYbMPzeLT1AQCKl8pkQkYm47d552Ox0VqVPdVROBDs0NDXebSB7D9bUsI_IJPZsrx-Hmuc-xfH8hokLbr4tmXeorlavEmLZJqBb1s3Z5Uuve8paQldQev5o7JbAEYPJj_Wgce8ftwiyAlUmU9vKt2RJTHIpmshcFNDBo3HLSsmchCI8ciT58nntrTWoYkApGly4w2"
        )
        {
            Log.i("LicenseInfo", it.message)
        }
        BarkoderHelper.applyConfigSettingsFromTemplate(
            context,
            binding.bkdView2.config, BarkoderConfigTemplate.INDUSTRIAL_1D, null
        )
        binding.bkdView2.startScanning(this)

        setActiveBarcodeTypes()
        setBarkoderSettings()
        return binding.root
    }

    private fun setActiveBarcodeTypes() {
        // There is option to set multiple active barcodes at once as array
        binding.bkdView2.config.decoderConfig.SetEnabledDecoders(
            arrayOf(
                Barkoder.DecoderType.QR,
                Barkoder.DecoderType.Ean13
            )
        )
        // or configure them one by one
        binding.bkdView2.config.decoderConfig.UpcA.enabled = true
    }

    private fun setBarkoderSettings() {
        // These are optional settings, otherwise default values will be used
        binding.bkdView2.config.let { config ->
            config.isImageResultEnabled = true
            config.isLocationInImageResultEnabled = true
            config.isRegionOfInterestVisible = true
            config.isPinchToZoomEnabled = true
            config.setRegionOfInterest(5f, 5f, 90f, 90f)
        }
    }

    private fun updateUI(result: Barkoder.Result? = null, resultImage: Bitmap? = null) {
        var barcodeNum2 = result?.textualData
        binding.textView23.text = barcodeNum2
//        val bundle = Bundle()
//        bundle.putString("barcodeNum2", barcodeNum2)
//        val bundle = Bundle().apply {
//            putString("barcodeNum2", barcodeNum2)
//        }


        findNavController().previousBackStackEntry?.savedStateHandle?.set("barcodeNum2", barcodeNum2)
        findNavController().popBackStack()

    }

    override fun scanningFinished(results: Array<Barkoder.Result>, resultImage: Bitmap?) {

        if (results.isNotEmpty())
            updateUI(results[0], resultImage)
        else
            updateUI()
    }


}

