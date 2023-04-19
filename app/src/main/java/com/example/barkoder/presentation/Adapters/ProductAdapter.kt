package com.example.barkoder.presentation.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.barkoder.shoppingApp.net.databinding.ProductListItemBinding
import com.example.barkoder.data.room.ProductDataEntity

class ProductAdapter (private var list : List<ProductDataEntity>): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


    class ViewHolder(private val binding : ProductListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list : ProductDataEntity) {
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

    fun setNotesList(lista : List<ProductDataEntity>) {
        this.list = lista
        notifyDataSetChanged()
    }

    fun getProductInt(position : Int): ProductDataEntity {
        return list[position]
    }

}