package com.example.barkodershopapp.presentation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.ActivitySelectProductBinding
import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.domain.userdataacc.Product
import com.example.barkodershopapp.presentation.Adapters.ProductAdapter
import com.example.barkodershopapp.presentation.Adapters.SelectProductAdapter
import com.example.barkodershopapp.presentation.viewmodel.ProductApiViewModel
import com.example.barkodershopapp.presentation.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.notifyAll
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SelectProductActivity : AppCompatActivity() {
    lateinit var binding : ActivitySelectProductBinding
    val productApiViewModel : ProductApiViewModel by viewModels()
    val productViewModel : ProductViewModel by viewModels()
    lateinit var pArrayList : ArrayList<Product>
    lateinit var nArrayList : ArrayList<Product>
    lateinit var selectAdapter : SelectProductAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectProductBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var productS = ArrayList<Product>()
        selectAdapter = SelectProductAdapter(productS, selectProductBtn)

        selectAdapter.setProductsList2(productS)

        binding.recViewSelect.apply {
            layoutManager = LinearLayoutManager(this@SelectProductActivity)
            adapter = selectAdapter
        }
        productApiViewModel.getAllProductsApi()

        productApiViewModel.resp.observe(this, { products ->
            selectAdapter.setProductsList2(products.products as ArrayList<Product>)
            searchView(products.products)
        })







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
            var intent = Intent(this@SelectProductActivity, ListProductActivity::class.java)
            startActivity(intent)
        }

    }

}