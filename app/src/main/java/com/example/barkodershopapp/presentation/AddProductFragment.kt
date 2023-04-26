package com.example.barkodershopapp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentAddProductBinding
import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.presentation.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddProductFragment : Fragment() {
    lateinit var binding : FragmentAddProductBinding
    val productViewModel : ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddProductBinding.inflate(inflater, container, false)


        val barcodeNumber = arguments?.getString("barcodeNum")
        binding.editTextBarcodeAddProduct.setText(barcodeNumber)


        return binding.root




    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var currentAddProduct = ProductDataEntity("productName","productBarcode","productNotes", 0, true, "https://st4.depositphotos.com/14953852/24787/v/600/depositphotos_247872612-stock-illustration-no-image-available-icon-vector.jpg", 1, 0)




        binding.btnScanAddProduct.setOnClickListener {
            findNavController().navigate(R.id.action_addProductFragment_to_scanFragment2)
        }


        binding.btnAddProductToList.setOnClickListener {

            var productName = binding.editTextNameAddProduct.text.toString()
            var productBarcode = binding.editTextBarcodeAddProduct.text.toString()
            var productPrice = binding.editPriceAddProduct.text.toString()
            var productNotes = binding.editTextNotesAddProduct.text.toString()

            if(productName.isNotEmpty() && productNotes.isNotEmpty() && productPrice.isNotEmpty()) {
                if(binding.checkBox.isChecked) {
                    currentAddProduct.activeProduct = true
                } else {
                    currentAddProduct.activeProduct = false
                }
                currentAddProduct.nameProduct = productName
                currentAddProduct.barcodeProduct = productBarcode
                currentAddProduct.priceProduct = productPrice.toInt()
                currentAddProduct.noteProduct = productNotes
                currentAddProduct.totalPrice = productPrice.toInt()

                productViewModel.insert(currentAddProduct)




            } else {
//                Toast.makeText(, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }


        }
    }
}