package com.example.barkodershopapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.barkoder.shoppingApp.net.databinding.ActivityPaymentBinding
import com.example.barkodershopapp.data.room.HistoryDataEntity
import com.example.barkodershopapp.presentation.viewmodel.HistoryViewModel
import com.example.barkodershopapp.presentation.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {
    lateinit var binding : ActivityPaymentBinding
    val historyViewModel : HistoryViewModel by viewModels()
    val productViewModel : ProductViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)


       var total = intent.getStringExtra("totalCost")
       var size = intent.getIntExtra("size", 0)
       var listName = intent.getStringExtra("listName")

        binding.btnBackActivityC.setOnClickListener {
            finish()
        }

        binding.textTotal.text = total
        binding.textSize.text = size.toString()
        binding.textListNamee.text = listName

        binding.btnCheckout.setOnClickListener {
            checkout()

        }


    }

    private fun checkout() {
        if(binding.editTextNameOnCard.text.toString() != ""
            && binding.editTextCardNumber.text.toString() != ""
            && binding.editTextExpiryDate.text.toString() != ""
            && binding.editTextCVC.text.toString() != "") {
            var nameList = binding.textListNamee.text.toString()
            var totalList = binding.textTotal.text.toString()
            var listSize = binding.textSize.text.toString()
            var date = "Pursched at ${getCurrentDate()}"
            val currentHistory = HistoryDataEntity(nameList,date,totalList,listSize)
            historyViewModel.insert(currentHistory)
            productViewModel.delete()

            var intent = Intent(this@PaymentActivity, HomeScreenActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "Chekcout was sucesfful", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return formatter.format(currentDate)
    }
}