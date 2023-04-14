package com.example.barkodershopapp.presentation.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.barkodershopapp.data.listhistorydata.HistoryListData
import com.example.barkodershopapp.data.listproductdata.ProductData
import com.example.barkodershopapp.databinding.HistorylistItemBinding
import com.example.barkodershopapp.databinding.ProductListItemBinding

class ProductAdapter (private var list : List<ProductData>): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(private val binding : ProductListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list : ProductData) {
            binding.textProductNamee.text = list.nameProduct
            binding.textBarcodeProduct.text = list.barcodeProduct.toString()
            binding.textCountProduct.text = list.count.toString()
            binding.textPriceProduct.text = list.priceProduct
            binding.textProductTotalPrice.text = list.priceProduct

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setNotesList(lista : List<ProductData>) {
        this.list = lista
        notifyDataSetChanged()
    }

}