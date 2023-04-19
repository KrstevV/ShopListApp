package com.example.barkoder.presentation

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.barkoder.Barkoder
import com.barkoder.BarkoderConfig
import com.barkoder.BarkoderHelper
import com.barkoder.enums.BarkoderConfigTemplate
import com.barkoder.interfaces.BarkoderResultCallback
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.ActivityScanProductBinding
import com.example.barkoder.presentation.viewmodel.ProductViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanProductActivity : AppCompatActivity(), BarkoderResultCallback {
    lateinit var binding : ActivityScanProductBinding
    val productViewModel : ProductViewModel by viewModels()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bkdView.config = BarkoderConfig(this, "PEmBIohr9EZXgCkySoetbwP4gvOfMcGzgxKPL2X6uqPEh7C-NSQGuK_IHt6EYbMPzeLT1AQCKl8pkQkYm47d552Ox0VqVPdVROBDs0NDXebSB7D9bUsI_IJPZsrx-Hmuc-xfH8hokLbr4tmXeorlavEmLZJqBb1s3Z5Uuve8paQldQev5o7JbAEYPJj_Wgce8ftwiyAlUmU9vKt2RJTHIpmshcFNDBo3HLSsmchCI8ciT58nntrTWoYkApGly4w2")
        { Log.i("LicenseInfo", it.message)
        }
        BarkoderHelper.applyConfigSettingsFromTemplate( this,
            binding.bkdView.config, BarkoderConfigTemplate.INDUSTRIAL_1D, null
        )

        binding.bkdView.startScanning(this)

        setActiveBarcodeTypes()
        setBarkoderSettings()



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
        binding.textBarcodeResult.text = result?.textualData
        var intentY = Intent(this@ScanProductActivity, SaveProductActivity::class.java)
        var barcodeNum = result?.textualData
        intentY.putExtra("barcodeNumber", barcodeNum)
        startActivity(intentY)

    }
    override fun scanningFinished(results: Array<Barkoder.Result>, resultImage: Bitmap?) {

        if (results.isNotEmpty())
            updateUI(results[0], resultImage)
        else
            updateUI()
    }


}