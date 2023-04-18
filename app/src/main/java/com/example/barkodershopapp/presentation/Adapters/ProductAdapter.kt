package com.example.barkodershopapp.presentation.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.barkodershopapp.data.room.HistoryDataEntity
import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.databinding.ProductListItemBinding

class ProductAdapter (private var list : List<ProductDataEntity>): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


    private val differCallBack = object : DiffUtil.ItemCallback<ProductDataEntity>() {
        override fun areItemsTheSame(
            oldItem: ProductDataEntity,
            newItem: ProductDataEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProductDataEntity,
            newItem: ProductDataEntity
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallBack)


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