package com.example.barkodershopapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

@AndroidEntryPoint
class SelectProductActivity : AppCompatActivity() {
    lateinit var binding : ActivitySelectProductBinding
    val productApiViewModel : ProductApiViewModel by viewModels()
    val productViewModel : ProductViewModel by viewModels()
    lateinit var selectAdapter : SelectProductAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectProductBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var productS = arrayListOf<Product>()
        selectAdapter = SelectProductAdapter(productS, selectProductBtn)

        binding.recViewSelect.apply {
            layoutManager = LinearLayoutManager(this@SelectProductActivity)
            adapter = selectAdapter
        }
        productApiViewModel.getAllProductsApi()

        productApiViewModel.resp.observe(this, {products ->
            selectAdapter.setProductsList(products.products)
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