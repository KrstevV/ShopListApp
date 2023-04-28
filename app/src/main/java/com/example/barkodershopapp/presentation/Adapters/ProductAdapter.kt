package com.example.barkodershopapp.presentation.Adapters

import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.barkoder.shoppingApp.net.databinding.ProductListItemBinding
import com.example.barkodershopapp.OnClickListenerButtons
import com.example.barkodershopapp.data.room.ListDataEntity
import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.typeconverters.TypeConverterss
import com.squareup.picasso.Picasso

class ProductAdapter (private var list : List<ListDataEntity>, private val listener : OnClickListenerButtons): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


    class ViewHolder(private var binding : ProductListItemBinding, private val listener: OnClickListenerButtons) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list : ListDataEntity) {
            binding.btnAddSize.setOnClickListener {
                listener.onClickPlus(list.listProducts)
            }
            binding.btnMinusSize.setOnClickListener {
                listener.onClickMinus(list.listProducts)
            }
            binding.textProductName.text = list.listProducts.nameProduct
            binding.textProductBarcode.text = list.listProducts.barcodeProduct.toString()
            binding.textCountProduct.text = list.listProducts.count.toString()
            binding.textProductPrice.text = list.listProducts.priceProduct.toString()
            binding.textProductTotalPrice.text = list.listProducts.totalPrice.toString()

            val byteArray = list.listProducts.imageProduct?.let { TypeConverterss.toBitmap(it) }
            binding.imageProduct.load(byteArray) {
                crossfade(true)
            }
            }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setNotesList(lista : List<ListDataEntity>) {
        this.list = lista
        notifyDataSetChanged()
    }

    fun getProductInt(position : Int): ListDataEntity {
        return list[position]
    }

}