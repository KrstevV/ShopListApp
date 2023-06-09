package com.example.barkodershopapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentProductInfoBinding
import com.example.barkodershopapp.data.db.pricedb.PriceHistory
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import com.example.barkodershopapp.ui.adapters.PriceHistoryAdapter
import com.example.barkodershopapp.ui.typeconverters.TypeConverterss
import com.example.barkodershopapp.ui.viewmodels.ProductViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class ProductInfoFragment : Fragment() {
    private val args by navArgs<ProductInfoFragmentArgs>()
    lateinit var binding : FragmentProductInfoBinding
    private val productViewModel: ProductViewModel by viewModels()
    lateinit var priceAdapter: PriceHistoryAdapter
    private lateinit var callback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentProductInfoBinding.inflate(inflater, container, false)
//        setupRecView()
        setupTextViews()
        onBackButton()
        navInvisible()

        requireActivity().title = requireContext().getString(R.string.updateProduct)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnUpdate.setOnClickListener {
            updateProduct()
        }


    }

    private fun navInvisible(){
        var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
        bottomNav.visibility = View.GONE
        var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        bottomFab.visibility = View.GONE
    }


    private fun setupTextViews(){
        binding.editTextNameUpdateProduct.setText(args.currentProduct.nameProduct)
        binding.editTextBarcodeUpdateProduct.setText(args.currentProduct.barcodeProduct)
        binding.editPriceUpdateProduct.setText(args.currentProduct.priceProduct.toString())
        binding.editQuantityUpdateProduct.setText(args.currentProduct.quantityProduct.toString())
        binding.editUnitUpdateProduct.setText(args.currentProduct.unitProduct)

        val byteArray = args.currentProduct.imageProduct?.let { TypeConverterss.toBitmap(it) }
        binding.imageProductInfo.load(byteArray) {
            crossfade(true)
        }
    }



//    private fun setupRecView(){
//        priceAdapter = PriceHistoryAdapter(args.currentProduct.priceHistory)
//        binding.recViewHistoryProduct.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = priceAdapter
//        }


//    }
    @SuppressLint("SuspiciousIndentation")
    private fun updateProduct() {
        var name = binding.editTextNameUpdateProduct.text.toString()
        var barcode = binding.editTextBarcodeUpdateProduct.text.toString()
        var price = binding.editPriceUpdateProduct.text.toString()
        var unit = binding.editUnitUpdateProduct.text.toString()
        var quantity = binding.editQuantityUpdateProduct.text.toString()

        if (price.toInt() != args.currentProduct.priceProduct) {
            var arrowResId = if (price.toInt() > args.currentProduct.priceProduct) {
                R.drawable.arrow_up
            } else {
                R.drawable.arrow_down
            }
            args.currentProduct.priceHistory.add(PriceHistory(price + " $", getCurrentDate(), arrowResId))

        }

        var currentProduct = ProductDataEntity(
            name,
            barcode,
            getCurrentDate(),
            price.toInt(),
            unit,
            quantity.toInt(),
            false,
            args.currentProduct.imageProduct,
            1,
            price.toInt(), args.currentProduct.priceHistory, 0, args.currentProduct.id
        )
        productViewModel.updateItem(currentProduct)

        findNavController().popBackStack()
    }
    private fun onBackButton(){
        callback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {

                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }


    private fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return formatter.format(currentDate)
    }

    override fun onPause() {
        super.onPause()
        var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
        bottomNav.visibility = View.VISIBLE
        var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        bottomFab.visibility = View.VISIBLE
    }
}