package com.example.barkodershopapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentProductHistoryBinding
import com.example.barkodershopapp.data.db.pricedb.PriceHistory
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import com.example.barkodershopapp.ui.adapters.PriceHistoryAdapter
import com.example.barkodershopapp.ui.viewmodels.ProductViewModel
import com.example.barkodershopapp.ui.typeconverters.TypeConverterss
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ProductHistoryFragment : Fragment() {
    lateinit var priceAdapter: PriceHistoryAdapter
    private val args by navArgs<ProductHistoryFragmentArgs>()
    lateinit var binding: FragmentProductHistoryBinding
    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductHistoryBinding.inflate(inflater, container, false)

        setupRecView()
        setupTextViews()


        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

    private fun setupTextViews(){
        binding.textActivityName.setText(args.currentProduct.nameProduct)
        binding.textNameInfo.setText(args.currentProduct.nameProduct)
        binding.textBarcodeInfo.setText(args.currentProduct.barcodeProduct)
        binding.textPriceInfo.setText(args.currentProduct.priceProduct.toString())
        binding.textQuantityInfo.setText(args.currentProduct.quantityProduct.toString())
        binding.textUnitInfo.setText(args.currentProduct.unitProduct)

        val byteArray = args.currentProduct.imageProduct?.let { TypeConverterss.toBitmap(it) }
        binding.imageProductInfo.load(byteArray) {
            crossfade(true)
        }
    }



    private fun setupRecView(){
        priceAdapter = PriceHistoryAdapter(args.currentProduct.priceHistory)
        priceAdapter.setPricesList(args.currentProduct.priceHistory)
        binding.recViewPrice.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = priceAdapter
        }


    }







    }



