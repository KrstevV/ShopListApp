package com.example.barkodershopapp.presentation

import android.content.Intent
import android.graphics.Canvas
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentListProductsBinding
import com.barkoder.shoppingApp.net.databinding.ToolBarBinding
import com.example.barkodershopapp.OnClickListenerButtons
import com.example.barkodershopapp.data.listhistorydata.swipecallback.SwipeToDelete
import com.example.barkodershopapp.data.room.HistoryDataEntity
import com.example.barkodershopapp.data.room.ListDataEntity
import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.presentation.Adapters.HistoryAdapter
import com.example.barkodershopapp.presentation.Adapters.ProductAdapter
import com.example.barkodershopapp.presentation.viewmodel.HistoryViewModel
import com.example.barkodershopapp.presentation.viewmodel.ListViewModel
import com.example.barkodershopapp.presentation.viewmodel.ProductApiViewModel
import com.example.barkodershopapp.presentation.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ListProductsFragment : Fragment() {
        lateinit var binding : FragmentListProductsBinding
    private lateinit var toolBarBind : ToolBarBinding
    lateinit var historyAdatper : HistoryAdapter
    lateinit var productAdapter: ProductAdapter
    val historyViewModel : HistoryViewModel by viewModels()
    val productViewModel : ProductViewModel by viewModels()
    val listViewModel : ListViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var editMode = requireActivity().intent.getBooleanExtra("editMode", false)
        var listId = requireActivity().intent.getLongExtra("currentListId", 0)




            var productL = arrayListOf<ListDataEntity>()

            productAdapter = ProductAdapter(productL, clickListenerButtons)

            binding.recViewProductList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = productAdapter
            }

            listViewModel.allNotes.observe(viewLifecycleOwner, { products ->
                productAdapter.setNotesList(products)


            })

        if(editMode) {
            binding.textActivityName.text = "Update List"
            binding.btnAddNewList.visibility = View.INVISIBLE
            binding.btnUpdateList.visibility = View.VISIBLE

        }

        binding.btnUpdateList.setOnClickListener {
            val listName = binding.editTextListName.text.toString()
            listViewModel.allNotes.observe(viewLifecycleOwner, {products4 ->
                var currentList = HistoryDataEntity(listName, getCurrentDate()
                    , products4 as ArrayList<ListDataEntity>, listId)

                historyViewModel.updateItem(currentList)
            })
            val intent = Intent(activity, HomeScreenActivity::class.java)
            startActivity(intent)
            requireActivity().finish()

        }




            binding.btnAddNewList.setOnClickListener {
                val listName = binding.editTextListName.text.toString()
                listViewModel.allNotes.observe(viewLifecycleOwner, { products3 ->

                    historyViewModel.insert(
                        HistoryDataEntity(
                            listName,
                            getCurrentDate(),
                            products3 as ArrayList<ListDataEntity>
                        )
                    )
                })

                val intent = Intent(activity, HomeScreenActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

            }


            val itemTouchHelper = ItemTouchHelper(swipteToDelete)
            itemTouchHelper.attachToRecyclerView(binding.recViewProductList)



    }

    private fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return formatter.format(currentDate)
    }

    private fun onLoop(products : MutableList<ProductDataEntity>) : Int {
        var sum = 0
        for(product in products) {
            sum += product.totalPrice
        }
        return sum
    }
    private val swipteToDelete = object : SwipeToDelete() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.absoluteAdapterPosition
            productAdapter.notifyItemRemoved(position)
            listViewModel.deleteItem(productAdapter.getProductInt(position))


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
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireActivity(),
                R.color.designColor))
                .addSwipeLeftActionIcon(R.drawable.delete_item)
                .create()
                .decorate()


            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)



        }
    }

    private val clickListenerButtons = object : OnClickListenerButtons {
        override fun onClickPlus(list: ProductDataEntity) {
            list.count++
            list.totalPrice = list.totalPrice + list.priceProduct
            productViewModel.updateItem(list)
        }



        override fun onClickMinus(list: ProductDataEntity) {
            if(list.count >= 2) {
                list.count--
                list.totalPrice = list.totalPrice - list.priceProduct
            }
            productViewModel.updateItem(list)
        }
    }

//    private fun builderDeleteAllProducts(){
//        val builder = context?.let { AlertDialog.Builder(it) }
//        builder!!.setTitle("Delete all Products")
//        builder.setMessage("Do you want to delete all Products?")
//        builder.setPositiveButton("Yes") { dialog, which ->
//            productViewModel.delete()
//            dialog.dismiss()
//        }
//        builder.setNegativeButton("No") { dialog, which ->
//            dialog.dismiss()
//        }
//        builder.show()
//    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentListProductsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onStop() {
        super.onStop()

    }
}