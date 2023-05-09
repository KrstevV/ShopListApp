package com.example.barkodershopapp.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
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
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentListProductsBinding
import com.barkoder.shoppingApp.net.databinding.ToolBarBinding
import com.example.barkodershopapp.ui.listeners.OnClickListenerButtons
import com.example.barkodershopapp.ui.listeners.swipecallback.SwipeToDelete
import com.example.barkodershopapp.data.db.historydatabase.HistoryDataEntity
import com.example.barkodershopapp.data.db.listdatabase.ListDataEntity
import com.example.barkodershopapp.ui.adapters.HistoryAdapter
import com.example.barkodershopapp.ui.adapters.ProductAdapter
import com.example.barkodershopapp.ui.viewmodels.HistoryViewModel
import com.example.barkodershopapp.ui.viewmodels.ListViewModel
import com.example.barkodershopapp.ui.viewmodels.ProductViewModel
import com.example.barkodershopapp.ui.activities.HomeScreenActivity
import com.example.barkodershopapp.ui.diffcallback.ProductDiffCallback
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
    private var oldList: List<ListDataEntity> = emptyList()
    private var newList: List<ListDataEntity> = emptyList()
    private var prevProducts: List<ListDataEntity>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListProductsBinding.inflate(inflater, container, false)
        setupRecView()
        observeList()



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        editMode()
        onClickButtonAdd()
        onClickButtonUpdate()
        setupSwipteDelete()

    }


    private fun setupSwipteDelete() {
        val itemTouchHelper = ItemTouchHelper(swipteToDelete)
        itemTouchHelper.attachToRecyclerView(binding.recViewProductList)
    }

    private fun setupRecView() {
        productAdapter = ProductAdapter(requireContext(),productL, clickListenerButtons)
        binding.recViewProductList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productAdapter
            binding.progressBar2.visibility = View.GONE
        }
    }

    private fun onClickButtonUpdate() {
        var listId = requireActivity().intent.getLongExtra("currentListId", 0)
        var checkedDate = requireActivity().intent.getStringExtra("checkedDate")
        binding.btnUpdateList.setOnClickListener {
            val listName = binding.editTextListName.text.toString()
            if (listName.isNotEmpty()) {
                listViewModel.allNotes.observe(viewLifecycleOwner, { products4 ->
                    var currentList = HistoryDataEntity(
                        listName, getCurrentDate(),
                        sumTotalCostList(products4).toString(),
                        checkedDate!!,
                        false, products4 as ArrayList<ListDataEntity>, listId
                    )
                    historyViewModel.updateItem(currentList)
                })
                val intent = Intent(activity, HomeScreenActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
                Toast.makeText(context, "List is successful updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "List name is empty!", Toast.LENGTH_SHORT).show()
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
                            "Not checked yet",
                            false,
                            products3 as ArrayList<ListDataEntity>
                        )
                    )
                })

                val intent = Intent(activity, HomeScreenActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
                Toast.makeText(context, "List is successful created", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "List name is empty!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeList() {
        listViewModel.allNotes.observe(viewLifecycleOwner, { products ->
                productAdapter.setNotesList(products as ArrayList<ListDataEntity>)
                binding.textTotalCostList.text = sumTotalCostList(products).toString() + " $"
                binding.progressBar2.visibility = View.GONE


        })
    }

    private fun editMode() {
        var editMode = requireActivity().intent.getBooleanExtra("editMode", false)
        var listName = requireActivity().intent.getStringExtra("listName")
        if (editMode) {
            binding.textActivityName.text = "Update List"
            binding.btnAddNewList.visibility = View.INVISIBLE
            binding.btnUpdateList.visibility = View.VISIBLE
            binding.editTextListName.setText(listName)
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

    private val swipteToDelete = object : SwipeToDelete() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.absoluteAdapterPosition
            val item = productAdapter.getProductInt(position)
            productAdapter.notifyItemRemoved(position)
            listViewModel.deleteItem(item)


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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                val builder = AlertDialog.Builder(context)
                    .setTitle("List Save")
                builder.setMessage("If you back to home screen ur list created will be delted!")
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





    override fun onResume() {
        super.onResume()
        binding.progressBar2.visibility = View.VISIBLE
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            binding.progressBar2.visibility = View.GONE
        }, 2000)


    }

    fun showAlert(context: Context) {

    }


}