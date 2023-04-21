package com.example.barkoder.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barkoder.shoppingApp.net.databinding.ActivityListProductBinding
import com.barkoder.shoppingApp.net.databinding.ToolBarBinding
import com.example.barkoder.OnClickListenerButtons
import com.example.barkoder.data.listhistorydata.swipecallback.SwipeToDelete
import com.example.barkoder.data.room.HistoryDataEntity
import com.example.barkoder.data.room.ProductDataEntity
import com.example.barkoder.data.room.ProductDatabase
import com.example.barkoder.presentation.Adapters.ProductAdapter
import com.example.barkoder.presentation.viewmodel.HistoryViewModel
import com.example.barkoder.presentation.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ListProductActivity : AppCompatActivity(){
    private lateinit var toolBarBind : ToolBarBinding
    lateinit var productAdatper : ProductAdapter
    lateinit var binding : ActivityListProductBinding
    val productViewModel : ProductViewModel by viewModels()
    val historyViewModel : HistoryViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var productL = arrayListOf<ProductDataEntity>()
        productAdatper = ProductAdapter(productL, clickListenerButtons)


        toolBarBind = ToolBarBinding.inflate(layoutInflater)
        var toolBar = toolBarBind.toolBarr
        setSupportActionBar(toolBar)


        binding.recViewProductList.apply {
            layoutManager = LinearLayoutManager(this@ListProductActivity)
            adapter = productAdatper
        }


        productViewModel.allNotes.observe(this, {products ->
            productAdatper.setNotesList(products)
            var top = onLoop(products).toString()
            binding.textTotalCost.text = top

        })



        binding.btnDeleteAll.setOnClickListener {
            builderDeleteAllProducts()
        }
        binding.btnBackActivity.setOnClickListener {
            finish()
        }

        binding.btnShopCart.setOnClickListener {
            if(binding.editTextListName.text.toString() != "") {
                var listName  = binding.editTextListName.text.toString()
                var size = productViewModel.allNotes.value?.size
                var intent = Intent(this@ListProductActivity, PaymentActivity::class.java)
                var totalCost = binding.textTotalCost.text
                intent.putExtra("listName", listName)
                intent.putExtra("totalCost", totalCost)
                intent.putExtra("size", size)
                startActivity(intent)
            }  else {
                   Toast.makeText(this, "Add a name on List", Toast.LENGTH_SHORT).show()
               }

        }


        binding.btnScan.setOnClickListener {
            var intent = Intent(this@ListProductActivity, ScanProductActivity::class.java)
            startActivity(intent)
        }

        val itemTouchHelper = ItemTouchHelper(swipteToDelete)

        itemTouchHelper.attachToRecyclerView(binding.recViewProductList)



        binding.btnAddList.setOnClickListener {

                    var intent = Intent(this@ListProductActivity, AddProduct::class.java)
                    startActivity(intent)
                    finish()

        }




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
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete all Products")
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


}