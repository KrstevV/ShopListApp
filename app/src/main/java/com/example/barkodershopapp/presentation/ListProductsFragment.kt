package com.example.barkodershopapp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentListProductsBinding
import com.barkoder.shoppingApp.net.databinding.ToolBarBinding
import com.example.barkodershopapp.OnClickListenerButtons
import com.example.barkodershopapp.data.listhistorydata.swipecallback.SwipeToDelete
import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.presentation.Adapters.ProductAdapter
import com.example.barkodershopapp.presentation.viewmodel.HistoryViewModel
import com.example.barkodershopapp.presentation.viewmodel.ProductApiViewModel
import com.example.barkodershopapp.presentation.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListProductsFragment : Fragment() {
        lateinit var binding : FragmentListProductsBinding
    private lateinit var toolBarBind : ToolBarBinding
    lateinit var productAdatper : ProductAdapter
    val productViewModel : ProductViewModel by viewModels()
    val historyViewModel : HistoryViewModel by viewModels()
    val productApiViewModel : ProductApiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var productL = arrayListOf<ProductDataEntity>()
        productAdatper = ProductAdapter(productL, clickListenerButtons)

        toolBarBind = ToolBarBinding.inflate(layoutInflater)
        var toolBar = toolBarBind.toolBarr


        binding.recViewProductList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productAdatper
        }

        productViewModel.allNotes.observe(viewLifecycleOwner, {products ->
            productAdatper.setNotesList(products)
            var top = onLoop(products).toString()
            binding.textTotalCost.text = top

        })

        binding.btnDeleteAll.setOnClickListener {
            builderDeleteAllProducts()
        }
        binding.btnBackActivity.setOnClickListener {

        }

        binding.btnShopCart.setOnClickListener {

            goToShopCart()
        }

        binding.btnScan.setOnClickListener {
            findNavController().navigate(R.id.action_listProductsFragment_to_scanFragment)
        }

        val itemTouchHelper = ItemTouchHelper(swipteToDelete)
        itemTouchHelper.attachToRecyclerView(binding.recViewProductList)

    }

    private fun goToShopCart() {
//        if(binding.editTextListName.text.toString() != "") {
//            var listName  = binding.editTextListName.text.toString()
//            var size = productViewModel.allNotes.value?.size
//            var intent = Intent(this@ListProductActivity, PaymentActivity::class.java)
//            var totalCost = binding.textTotalCost.text
//            intent.putExtra("listName", listName)
//            intent.putExtra("totalCost", totalCost)
//            intent.putExtra("size", size)
//            startActivity(intent)
//        }  else {
//            Toast.makeText(this, "Add a name on Cart", Toast.LENGTH_SHORT).show()
//        }
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
            productAdatper.notifyItemRemoved(position)
            productViewModel.deleteItem(productAdatper.getProductInt(position))
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

    private fun builderDeleteAllProducts(){
        val builder = context?.let { AlertDialog.Builder(it) }
        builder!!.setTitle("Delete all Products")
        builder.setMessage("Do you want to delete all Products?")
        builder.setPositiveButton("Yes") { dialog, which ->
            productViewModel.delete()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentListProductsBinding.inflate(inflater, container, false)
        return binding.root
    }
}