package com.example.barkodershopapp.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentProductHistoryBinding
import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.presentation.Adapters.PriceHistoryAdapter
import com.example.barkodershopapp.presentation.Adapters.SelectProductAdapter
import com.example.barkodershopapp.presentation.viewmodel.ProductViewModel
import com.example.barkodershopapp.typeconverters.TypeConverterss
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductHistoryFragment : Fragment() {
    lateinit var priceAdapter : PriceHistoryAdapter
    var productH = arrayListOf<PriceData>()
    private val args by navArgs<ProductHistoryFragmentArgs>()
        lateinit var binding : FragmentProductHistoryBinding
        val productViewModel : ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductHistoryBinding.inflate(inflater, container, false)



        productH.add(PriceData(args.currentProduct.priceProduct.toString()))
        productH.add(PriceData("920"))



        priceAdapter = PriceHistoryAdapter(productH)

//        listHistoryPrice.add(PriceData("123"))
//        priceAdapter.setPricesList(productH)

        binding.recViewHistoryProduct.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = priceAdapter
        }

//        priceAdapter.setPricesList(productH)



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

    }


    private fun updateProduct(){
        var name = binding.editTextNameEditProduct.text.toString()
        var barcode = binding.editTextBarcodeEditProduct.text.toString()
        var price = binding.editPriceEditProduct.text.toString()
        val currentProduct = ProductDataEntity(name,
            barcode,
            "asdsadada",
            price.toInt(),
            true,
            args.currentProduct.imageProduct,
            1,
            30, args.currentProduct.id)

        productH.add(PriceData(args.currentProduct.priceProduct.toString()))


        productViewModel.updateItem(currentProduct)

        val bundle2 = Bundle()
        bundle2.putString("updatedName", currentProduct.nameProduct)
        bundle2.putString("updatedBarcode", currentProduct.barcodeProduct)
        bundle2.putString("updatedPrice", currentProduct.priceProduct.toString())
        bundle2.putByteArray("updatedImage", currentProduct.imageProduct)

        findNavController().navigate(R.id.selectProductFragment, bundle2)





    }

}