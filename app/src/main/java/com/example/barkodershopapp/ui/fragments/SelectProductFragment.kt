package com.example.barkodershopapp.ui.fragments

import android.app.AlertDialog
import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentSelectProductBinding
import com.example.barkodershopapp.ui.listeners.swipecallback.SwipeToDelete
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import com.example.barkodershopapp.ui.adapters.SelectProductAdapter
import com.example.barkodershopapp.ui.viewmodels.ListViewModel
import com.example.barkodershopapp.ui.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

@AndroidEntryPoint
class SelectProductFragment : Fragment() {
    val productViewModel: ProductViewModel by viewModels()
    lateinit var selectAdapter: SelectProductAdapter
    lateinit var binding: FragmentSelectProductBinding
    val listViewMOdel: ListViewModel by viewModels()
    var productS = ArrayList<ProductDataEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSelectProductBinding.inflate(inflater, container, false)

        setupRecView()
        observeList()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipteDelete()

    }
    private fun swipteDelete(){
        val itemTouchHelper = ItemTouchHelper(swipteToDelete)
        itemTouchHelper.attachToRecyclerView(binding.recViewSelect)
    }
    private fun observeList(){
        productViewModel.allNotes.observe(viewLifecycleOwner, Observer { products ->
            binding.progressBar3.visibility = View.GONE
            selectAdapter.setProductsList2(products as ArrayList<ProductDataEntity>)
            searchView(products)
        })
    }
    private fun setupRecView(){
        selectAdapter = SelectProductAdapter(productS, listViewMOdel)
        selectAdapter.setProductsList2(productS)

        binding.recViewSelect.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = selectAdapter
        }
    }

    private val swipteToDelete = object : SwipeToDelete() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.absoluteAdapterPosition
            selectAdapter.notifyItemRemoved(position)
            productViewModel.deleteItem(selectAdapter.getSelectInt(position))
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {

            RecyclerViewSwipeDecorator.Builder(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive

            )
                .addSwipeLeftBackgroundColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.designColor
                    )
                )
                .addSwipeLeftActionIcon(R.drawable.delete_item)
                .create()
                .decorate()
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                val builder = AlertDialog.Builder(context)
                    .setTitle("List Save")
                builder.setMessage("If you back to home screen ur list create will be delted!")
                    .setCancelable(false)
                    .setNegativeButton("stay") { dialog, id ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("OK") { dialog, id ->
                        requireActivity().finish()
                        dialog.dismiss()
                    }

                val alert = builder.create()
                alert.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}

