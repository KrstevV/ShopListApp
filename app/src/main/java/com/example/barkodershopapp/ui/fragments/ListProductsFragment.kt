package com.example.barkodershopapp.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentListProductsBinding
import com.example.barkodershopapp.ui.listeners.OnClickListenerButtons
import com.example.barkodershopapp.ui.listeners.swipecallback.SwipeToDelete
import com.example.barkodershopapp.data.db.historydatabase.HistoryDataEntity
import com.example.barkodershopapp.data.db.listdatabase.ListDataEntity
import com.example.barkodershopapp.ui.activities.MainActivity
import com.example.barkodershopapp.ui.adapters.ProductAdapter
import com.example.barkodershopapp.ui.listeners.swipeicons.SwipeHelper
import com.example.barkodershopapp.ui.viewmodels.HistoryViewModel
import com.example.barkodershopapp.ui.viewmodels.ListViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ListProductsFragment : Fragment() {
    lateinit var binding: FragmentListProductsBinding
    lateinit var productAdapter: ProductAdapter
    private val historyViewModel: HistoryViewModel by viewModels()
    private val listViewModel: ListViewModel by viewModels()
    private val productL = arrayListOf<ListDataEntity>()
    var editMode = this@ListProductsFragment.arguments?.getBoolean("editMode")


    private lateinit var callback: OnBackPressedCallback



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListProductsBinding.inflate(inflater, container, false)
        setupRecView()
        observeList()
        onBackButton()



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editMode()
        onClickButtonAdd()
        onClickButtonUpdate()
        navInvisible()
        onButtonSelect()

    }

    private fun onButtonSelect(){
        binding.buttonImage.setOnClickListener {
            var listId = this@ListProductsFragment.arguments?.getLong("currentListId")
            var checkedDate = this@ListProductsFragment.arguments?.getString("checkedDate").toString()
            var listName = this@ListProductsFragment.arguments?.getString("listName").toString()
            var edit = this@ListProductsFragment.arguments?.getBoolean("edit")
            var editMode = this@ListProductsFragment.arguments?.getBoolean("editMode")
            if(editMode == true) {
                var bundle = Bundle()
                bundle.putBoolean("editSelect", true)
                bundle.putLong("listId",listId!!)
                bundle.putString("checkedDate", checkedDate)
                bundle.putString("listName", listName)
                findNavController().navigate(
                    R.id.selectProductFragment,
                    bundle,
                    NavOptions.Builder().setPopUpTo(R.id.listProductsFragment, true).build()
                )
            } else {
                var bundle = Bundle()
                bundle.putBoolean("createSelect", true)
                bundle.putString("listNameCreate", binding.editTextListName.text.toString())
                findNavController().navigate(
                    R.id.selectProductFragment,
                    bundle,
                    NavOptions.Builder().setPopUpTo(R.id.listProductsFragment, true).build()
                )
            }
            }
        }

    private fun navInvisible(){
        var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
        bottomNav.visibility = View.GONE
        var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        bottomFab.visibility = View.GONE
    }

    private fun setupRecView() {
        productAdapter = ProductAdapter(requireContext(),productL, clickListenerButtons)
        binding.recViewProductList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productAdapter
            binding.progressBar2.visibility = View.GONE

            val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.recViewProductList) {
                override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                    var buttons = listOf<UnderlayButton>()
                    val deleteButton = deleteButton(position)

                    buttons = listOf(deleteButton)
                    return buttons
                }
            })

            itemTouchHelper.attachToRecyclerView(binding.recViewProductList)
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
                AlertDialog.Builder(context)
                    .setTitle("Delete Product")
                    .setMessage("Are you sure you want to delete this product?")
                    .setPositiveButton("Delete") { _, _ ->
                        productAdapter.notifyItemRemoved(position)
                        listViewModel.deleteItem(productAdapter.getProductInt(position))
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        })
}

    private fun onClickButtonUpdate() {
        var listId = this@ListProductsFragment.arguments?.getLong("currentListId")
        var checkedDate = this@ListProductsFragment.arguments?.getString("checkedDate").toString()
        binding.btnUpdateList.setOnClickListener {
            val listName = binding.editTextListName.text.toString()
            if (listName.isNotEmpty()) {
                listViewModel.allNotes.observe(viewLifecycleOwner, { products4 ->
                    var currentList = HistoryDataEntity(
                        listName, getCurrentDate(),
                        sumTotalCostList(products4).toString(),
                        checkedDate!!,
                        false, products4 as ArrayList<ListDataEntity>, listId!!
                    )
                    historyViewModel.updateItem(currentList)
                    listViewModel.delete()
                })
                findNavController().navigate(
                    R.id.historyListFragment2,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.listProductsFragment, true).build()
                )
                Toast.makeText(context, R.string.toast_list_sucessful_updated, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, R.string.toast_list_name_empty, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onClickButtonAdd() {
        binding.btnAddNewList.setOnClickListener {
            val listName = binding.editTextListName.text.toString()
            if (listName.isNotEmpty()) {
                listViewModel.allNotes.observe(viewLifecycleOwner, { products3 ->

                    historyViewModel.insert(
                        HistoryDataEntity(
                            listName,
                            getCurrentDate(),
                            sumTotalCostList(products3).toString(),
                            "Not checked yet!",
                            false,
                            products3 as ArrayList<ListDataEntity>
                        )
                    )
                })
                listViewModel.delete()

                findNavController().navigate(
                    R.id.historyListFragment2,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.listProductsFragment, true).build()
                )
                Toast.makeText(context, R.string.toast_list_secesfful_created, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, R.string.toast_list_name_empty, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeList() {
        listViewModel.allNotes.observe(viewLifecycleOwner, { products ->
                productAdapter.setNotesList(products as ArrayList<ListDataEntity>)
                binding.textTotalCostList.text = sumTotalCostList(products).toString() + " $"
                binding.textSizeProducstsList.text = products.size.toString()
                binding.progressBar2.visibility = View.GONE


        })
    }

    private fun editMode() {
        var editMode = this@ListProductsFragment.arguments?.getBoolean("editMode")
        var listName = this@ListProductsFragment.arguments?.getString("listName")
        var backUpdate = this@ListProductsFragment.arguments?.getBoolean("backUpdate")

        if (editMode == true) {
            binding.textActivityName.text = "Update List"
            binding.btnAddNewList.visibility = View.INVISIBLE
            binding.btnUpdateList.visibility = View.VISIBLE
            binding.editTextListName.setText(listName)
        } else {
            binding.textActivityName.text = "Create List"
            binding.btnAddNewList.visibility = View.VISIBLE
            binding.btnUpdateList.visibility = View.INVISIBLE
            binding.editTextListName.setText("")
        }
    }

    private fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return formatter.format(currentDate)
    }

    private fun sumTotalCostList(products: MutableList<ListDataEntity>): Int {
        var sum = 0
        for (product in products) {
            sum += product.listProducts.totalPrice
        }
        return sum
    }




    private fun onBackButton(){
        callback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {

                findNavController().navigate(
                    R.id.historyListFragment2,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.listProductsFragment, true).build()
                )
                listViewModel.delete()

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    private val clickListenerButtons = object : OnClickListenerButtons {
        override fun onClickPlus(list: ListDataEntity) {
            list.listProducts.count++
            list.listProducts.totalPrice =
                list.listProducts.totalPrice + list.listProducts.priceProduct
            listViewModel.updateItem(list)
        }

        override fun onClickMinus(list: ListDataEntity) {
            if (list.listProducts.count >= 2) {
                list.listProducts.count--
                list.listProducts.totalPrice =
                    list.listProducts.totalPrice - list.listProducts.priceProduct
            }
            listViewModel.updateItem(list)
        }
    }
    override fun onResume() {
        super.onResume()
        binding.progressBar2.visibility = View.VISIBLE
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            binding.progressBar2.visibility = View.GONE
        }, 2000)
    }

    override fun onPause() {
        super.onPause()

        var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
        bottomNav.visibility = View.VISIBLE
        var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        bottomFab.visibility = View.VISIBLE
    }





}