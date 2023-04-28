package com.example.barkodershopapp.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentProductHistoryBinding
import com.example.barkodershopapp.data.room.HistoryDataEntity
import com.example.barkodershopapp.data.room.ListDataEntity
import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.presentation.Adapters.PriceHistoryAdapter
import com.example.barkodershopapp.presentation.Adapters.SelectProductAdapter
import com.example.barkodershopapp.presentation.viewmodel.HistoryViewModel
import com.example.barkodershopapp.presentation.viewmodel.ListViewModel
import com.example.barkodershopapp.presentation.viewmodel.ProductViewModel
import com.example.barkodershopapp.typeconverters.TypeConverterss
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ProductHistoryFragment : Fragment() {
    lateinit var priceAdapter : PriceHistoryAdapter
    private val args by navArgs<ProductHistoryFragmentArgs>()
        lateinit var binding : FragmentProductHistoryBinding
        val productViewModel : ProductViewModel by viewModels()
        val historyViewModel : HistoryViewModel by viewModels()
        val listViewModel : ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductHistoryBinding.inflate(inflater, container, false)





        priceAdapter = PriceHistoryAdapter(args.currentProduct.priceHistory)

//        listHistoryPrice.add(PriceData("123"))
//        priceAdapter.setPricesList(productH)

        binding.recViewHistoryProduct.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = priceAdapter
        }


        priceAdapter.setPricesList(args.currentProduct.priceHistory)



        binding.editTextNameEditProduct.setText(args.currentProduct.nameProduct)
        binding.editTextBarcodeEditProduct.setText(args.currentProduct.barcodeProduct)
        binding.editPriceEditProduct.setText(args.currentProduct.priceProduct.toString())

        val byteArray = args.currentProduct.imageProduct?.let { TypeConverterss.toBitmap(it) }
        binding.imageEditProduct.load(byteArray) {
            crossfade(true)
        }
        return  binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnUpdate.setOnClickListener {
            updateProduct()

        }
        binding.buttonADD.setOnClickListener {
            insertProduct()
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun updateProduct(){
        var name = binding.editTextNameEditProduct.text.toString()
        var barcode = binding.editTextBarcodeEditProduct.text.toString()
        var price = binding.editPriceEditProduct.text.toString()
        val currentProduct = ProductDataEntity(name,
            barcode,
            getCurrentDate(),
            price.toInt(),
            true,
            args.currentProduct.imageProduct,
            1,
            30,args.currentProduct.priceHistory, args.currentProduct.id)

            args.currentProduct.priceHistory.add(price)

        productViewModel.updateItem(currentProduct)

        findNavController().navigate(R.id.selectProductFragment)

    }

    private fun insertProduct() {
        var name = binding.editTextNameEditProduct.text.toString()
        var barcode = binding.editTextBarcodeEditProduct.text.toString()
        var price = binding.editPriceEditProduct.text.toString()
        val currentProduct = ProductDataEntity(name,
            barcode,
            getCurrentDate(),
            price.toInt(),
            true,
            args.currentProduct.imageProduct,
            1,
            30,args.currentProduct.priceHistory, args.currentProduct.id)
        val listDataEntity = ListDataEntity(currentProduct)
        listViewModel.insert(listDataEntity)
        val navContr = findNavController()
        navContr.navigate(R.id.listProductsFragment, null, NavOptions.Builder().setPopUpTo(R.id.selectProductFragment, true).build())




    }

    private fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return formatter.format(currentDate)
    }

}