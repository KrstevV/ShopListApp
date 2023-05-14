package com.example.barkodershopapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentHomeScreenBinding
import com.example.barkodershopapp.data.db.historydatabase.HistoryDataEntity
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import com.example.barkodershopapp.ui.activities.MainActivity
import com.example.barkodershopapp.ui.adapters.*
import com.example.barkodershopapp.ui.viewmodels.HistoryViewModel
import com.example.barkodershopapp.ui.viewmodels.ListViewModel
import com.example.barkodershopapp.ui.viewmodels.ProductViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenFragment : Fragment() {
    val productViewModel: ProductViewModel by viewModels()
    lateinit var binding: FragmentHomeScreenBinding
    lateinit var productAdapter : HomeProductAdapter
    lateinit var listAdapter : HomeListAdapter
    var listList = arrayListOf<HistoryDataEntity>()
    var list = arrayListOf<ProductDataEntity>()
    val listViewMOdel: ListViewModel by viewModels()
    val historyViewModel : HistoryViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)

        recViewList()
        observeListList()
        recViewProduct()
        observeListProduct()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onCLickScan()

    }

    private fun recViewList(){
        listAdapter = HomeListAdapter(listList, listViewMOdel)
        listAdapter.setProductsList2(listList)

       var linear =  object : LinearLayoutManager(context) { override fun canScrollVertically() = false }

        binding.recViewCurrentlyLists.apply {
            layoutManager = linear

            adapter = listAdapter

        }
        binding.recViewCurrentlyLists.isNestedScrollingEnabled = false
    }

    private fun observeListList() {
        historyViewModel.allNotes.observe(viewLifecycleOwner, Observer { products ->
//            binding.progressBar3.visibility = View.GONE
            products.reverse()
            listAdapter.setProductsList2(products as ArrayList<HistoryDataEntity>)
//            searchView(products)
        })
    }

    private fun recViewProduct(){
        productAdapter = HomeProductAdapter(list, listViewMOdel)
        productAdapter.setProductsList2(list)

        var linear =  object : LinearLayoutManager(context) { override fun canScrollVertically() = false }

        binding.recViewCurentyProducts.apply {
            layoutManager = linear

            adapter = productAdapter

        }
        binding.recViewCurentyProducts.isNestedScrollingEnabled = false
    }

    private fun observeListProduct() {
        productViewModel.allNotes.observe(viewLifecycleOwner, Observer { products2 ->
//            binding.progressBar3.visibility = View.GONE
            products2.reverse()
            productAdapter.setProductsList2(products2 as ArrayList<ProductDataEntity>)
//            searchView(products)
        })
    }

    private fun onCLickScan() {
        var btnScan = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        btnScan.setOnClickListener {
            findNavController().navigate(
                R.id.scanFragment,
                null,
                NavOptions.Builder().setPopUpTo(R.id.homeScreenFragment, true).build()
            )
        }
    }

    override fun onPause() {
        super.onPause()

        observeListList()
        observeListProduct()
    }
}

