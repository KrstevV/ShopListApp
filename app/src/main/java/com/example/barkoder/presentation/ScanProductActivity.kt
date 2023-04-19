package com.example.barkoder.presentation

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.barkoder.Barkoder
import com.barkoder.BarkoderConfig
import com.barkoder.BarkoderHelper
import com.barkoder.enums.BarkoderConfigTemplate
import com.barkoder.interfaces.BarkoderResultCallback
import com.barkoder.shoppingApp.net.databinding.ActivityScanProductBinding
import com.example.barkoder.presentation.viewmodel.ProductViewModel
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




    }

    override fun scanningFinished(p0: Array<out Barkoder.Result>?, p1: Bitmap?) {
        p0?.get(0)?.let { Log.i("Scanned result", it.textualData) }
    }
}