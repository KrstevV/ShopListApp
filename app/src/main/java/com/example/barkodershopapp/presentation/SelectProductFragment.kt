package com.example.barkodershopapp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentSelectProductBinding
import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.domain.userdataacc.Product
import com.example.barkodershopapp.presentation.Adapters.SelectProductAdapter
import com.example.barkodershopapp.presentation.viewmodel.ProductApiViewModel
import com.example.barkodershopapp.presentation.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectProductFragment : Fragment() {
    val productApiViewModel : ProductApiViewModel by viewModels()
    val productViewModel : ProductViewModel by viewModels()
    lateinit var selectAdapter : SelectProductAdapter
        lateinit var binding : FragmentSelectProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        var productS = ArrayList<Product>()
//
//        selectAdapter = SelectProductAdapter(productS, selectProductBtn)
//
//
//        selectAdapter.setProductsList2(productS)
//
//        binding.recViewSelect.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = selectAdapter
//        }
//        productApiViewModel.getAllProductsApi()
//
//        productApiViewModel.resp.observe(viewLifecycleOwner, { products ->
//            selectAdapter.setProductsList2(products.products as ArrayList<Product>)
//            searchView(products.products)
//        })
//
//        binding.btnBackActivity3.setOnClickListener {
//
//        }




    }

    private fun searchView(list : List<Product>) {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    selectAdapter.setProductsList(list as ArrayList<Product>)
                } else {
                    val filteredList = list.filter { product ->
                        product.name.contains(newText, ignoreCase = true)
                    } as ArrayList<Product>
                    selectAdapter.setProductsList(filteredList)
                }
                return false
            }

        })
    }







    private var selectProductBtn = object : OnClickSelectProduct {
        override fun onClickSelect(list: Product) {
            var currentProduct2 = ProductDataEntity(list.name, list.barcode,
                list.notes, list.price,true,list.image, list.count, list.totalprice)
            productViewModel.insert(currentProduct2)
            findNavController().navigate(R.id.listProductsFragment)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentSelectProductBinding.inflate(inflater, container, false)

        var productS = ArrayList<Product>()

        selectAdapter = SelectProductAdapter(productS, selectProductBtn)


        selectAdapter.setProductsList2(productS)

        binding.recViewSelect.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = selectAdapter
        }

        productApiViewModel.getAllProductsApi()

        productApiViewModel.resp.observe(viewLifecycleOwner, { products ->
            selectAdapter.setProductsList2(products.products as ArrayList<Product>)
            searchView(products.products)
        })

        binding.btnBackActivity3.setOnClickListener {

        }
        return  binding.root




    }




}

