package com.example.barkodershopapp.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentSelectProductBinding
import com.example.barkodershopapp.ui.listeners.swipecallback.SwipeToDelete
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import com.example.barkodershopapp.ui.activities.HomeScreenActivity
import com.example.barkodershopapp.ui.adapters.SelectProductAdapter
import com.example.barkodershopapp.ui.listeners.swipeicons.SwipeHelper
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


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecView()
        observeList()

    }

    private fun swipteDelete() {
        val itemTouchHelper = ItemTouchHelper(swipteToDelete)
        itemTouchHelper.attachToRecyclerView(binding.recViewSelect)
    }

    private fun observeList() {
        productViewModel.allNotes.observe(viewLifecycleOwner, Observer { products ->
            binding.progressBar3.visibility = View.GONE
            selectAdapter.setProductsList2(products as ArrayList<ProductDataEntity>)
            searchView(products)
        })
    }

    private fun setupRecView() {
        selectAdapter = SelectProductAdapter(productS, listViewMOdel)
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
//                    selectAdapter.notifyItemRemoved(position)
//                    productViewModel.deleteItem(selectAdapter.getSelectInt(position))
                    Toast.makeText(requireContext(), "Marked as unread item $position", Toast.LENGTH_SHORT)
                        .show()
                }
            })
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
                    val action = SelectProductFragmentDirections.actionSelectProductFragmentToProductInfoFragment(currentProduct)
                    Navigation.findNavController(binding.root).navigate(action,
                        NavOptions.Builder().setPopUpTo(R.id.selectProductFragment, true).build())
                }
            })
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
                var editMode = requireActivity().intent.getBooleanExtra("editMode", false)
                if (editMode) {
                    var intent = Intent(requireActivity(), HomeScreenActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {

                    val builder = AlertDialog.Builder(context)
                        .setTitle(R.string.alert_title_list_save)
                    builder.setMessage(R.string.alert_dialog_back)
                        .setCancelable(false)
                        .setNegativeButton(R.string.alert_title_list_save_negative) { dialog, id ->
                            dialog.dismiss()
                        }
                        .setPositiveButton(R.string.alert_title_list_save_positive) { dialog, id ->
                            requireActivity().finish()
                            dialog.dismiss()
                        }

                    val alert = builder.create()
                    alert.show()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}


