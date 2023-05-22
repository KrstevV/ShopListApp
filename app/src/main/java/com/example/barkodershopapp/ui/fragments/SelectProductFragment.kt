package com.example.barkodershopapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentSelectProductBinding
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import com.example.barkodershopapp.ui.adapters.SelectProductAdapter
import com.example.barkodershopapp.ui.listeners.swipeicons.SwipeHelper
import com.example.barkodershopapp.ui.viewmodels.ListViewModel
import com.example.barkodershopapp.ui.viewmodels.ProductViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SelectProductFragment : Fragment() {
    val productViewModel: ProductViewModel by viewModels()
    lateinit var selectAdapter: SelectProductAdapter
    lateinit var binding: FragmentSelectProductBinding
    val listViewMOdel: ListViewModel by viewModels()
    var productS = ArrayList<ProductDataEntity>()

    private lateinit var callback: OnBackPressedCallback


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSelectProductBinding.inflate(inflater, container, false)



        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecView()
        onCLickScan()
        observeList()



        var editSelect = this@SelectProductFragment.arguments?.getBoolean("editSelect")
        var createSelect = this@SelectProductFragment.arguments?.getBoolean("createSelect")



        if(editSelect == true) {
            selectAdapter.isEditMode = true
        } else {
            selectAdapter.isEditMode = false
        }


        if(editSelect == true || createSelect == true) {
            selectAdapter.showAddButton = true
                var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
                bottomNav.visibility = View.GONE
                var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
                bottomFab.visibility = View.GONE
                binding.guideline27.setGuidelinePercent(1F)
                onBackButton()

        } else {
            selectAdapter.showAddButton = false
            var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
            bottomNav.visibility = View.VISIBLE
            var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
            bottomFab.visibility = View.VISIBLE
        }
    }

    private fun observeList() {
        productViewModel.allNotes.observe(viewLifecycleOwner, Observer { products ->
            binding.progressBar3.visibility = View.GONE
            selectAdapter.setProductsList2(products as ArrayList<ProductDataEntity>)
            searchView(products)

        })
    }

    private fun setupRecView() {
        var listId = this@SelectProductFragment.arguments?.getLong("listId")
        var checkedDate = this@SelectProductFragment.arguments?.getString("checkedDate")
        var listName = this@SelectProductFragment.arguments?.getString("listName")
        var editSelect = this@SelectProductFragment.arguments?.getBoolean("editSelect")
        var listNameCreate = this@SelectProductFragment.arguments?.getString("listNameCreate")
        if(editSelect == true){
            selectAdapter = SelectProductAdapter(productS, listViewMOdel, listId!!, checkedDate!!,listName!!)
        } else {
            selectAdapter = SelectProductAdapter(productS, listViewMOdel, 0L, "","")
        }



        selectAdapter.setProductsList2(productS)

        binding.recViewSelect.apply {
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            layoutManager = LinearLayoutManager(context)
            adapter = selectAdapter

            val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.recViewSelect) {
                override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                    var buttons = listOf<UnderlayButton>()
                    val deleteButton = deleteButton(position)
                    val archiveButton = archiveButton(position)

                    buttons = listOf(deleteButton, archiveButton)
                    return buttons
                }
            })

            itemTouchHelper.attachToRecyclerView(binding.recViewSelect)
        }
    }

    private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            "Delete",
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    selectAdapter.notifyItemRemoved(position)
                    productViewModel.deleteItem(selectAdapter.getSelectInt(position))

                }
            })
    }

    private fun onCLickScan() {
        var btnScan = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        btnScan.setOnClickListener {
            findNavController().navigate(
                R.id.scanFragment,
                null,
                NavOptions.Builder().setPopUpTo(R.id.selectProductFragment, true).build()
            )
        }
    }

    private fun archiveButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            "Edit Product",
            14.0f,
            R.color.editButton,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    val currentProduct = selectAdapter.getSelectInt(position)
                    val action =
                        SelectProductFragmentDirections.actionSelectProductFragmentToProductInfoFragment(
                            currentProduct
                        )
                    Navigation.findNavController(binding.root).navigate(action)
                }
            })
    }


    private fun searchView(list: List<ProductDataEntity>) {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    selectAdapter.setProductsList(list as ArrayList<ProductDataEntity>)
                } else {
                    val filteredList = list.filter { product ->
                        product.nameProduct!!.contains(newText, ignoreCase = true)
                    } as ArrayList<ProductDataEntity>
                    selectAdapter.setProductsList(filteredList)
                }
                return false
            }
        })
    }

    private fun onBackButton(){
        callback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {
                var editMode = this@SelectProductFragment.arguments?.getBoolean("editMode")
                var bundle = Bundle()
                if(editMode == true) {
                    bundle.putBoolean("backUpdate", true)
                } else {
                    bundle.putBoolean("backUpdate", false)
                }

                        findNavController().navigate(
                            R.id.listProductsFragment,
                            bundle,
                            NavOptions.Builder().setPopUpTo(R.id.selectProductFragment, true).build()
                        )
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onPause() {
        super.onPause()

        var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
        bottomNav.visibility = View.VISIBLE
        var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        bottomFab.visibility = View.VISIBLE
    }




}


