package com.example.barkodershopapp.presentation.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.barkoder.shoppingApp.net.databinding.PricehistoryItemBinding
import com.barkoder.shoppingApp.net.databinding.SelectproductItemBinding
import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.presentation.PriceData
import com.example.barkodershopapp.presentation.SelectProductFragmentDirections
import com.example.barkodershopapp.typeconverters.TypeConverterss
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PriceHistoryAdapter (private var list : ArrayList<String>) : RecyclerView.Adapter<PriceHistoryAdapter.ViewHolder>(){


    fun setPricesList(pricesList: ArrayList<String>) {
        val updatedList = ArrayList<String>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentTime = dateFormat.format(Date())
        for (price in pricesList) {
            updatedList.add("$price ($currentTime)")
        }
        this.list = pricesList
        notifyDataSetChanged()
    }




    class ViewHolder(private val binding : PricehistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (list: String) {
            binding.textPriceHistory.setText(list)



        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceHistoryAdapter.ViewHolder {
        val binding = PricehistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PriceHistoryAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size




}

private fun getCurrentDate(): String {
    val currentDate = Calendar.getInstance().time
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
    return formatter.format(currentDate)
}