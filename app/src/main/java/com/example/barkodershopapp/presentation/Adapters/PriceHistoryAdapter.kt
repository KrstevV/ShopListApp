package com.example.barkodershopapp.presentation.Adapters

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

class PriceHistoryAdapter (private var list : ArrayList<PriceData>) : RecyclerView.Adapter<PriceHistoryAdapter.ViewHolder>(){


    fun setPricesList(pricesList: ArrayList<PriceData>) {
        this.list = pricesList
        notifyDataSetChanged()
    }


    class ViewHolder(private val binding : PricehistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (list: PriceData) {
            binding.textPriceHistory.text = list.productPrice

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