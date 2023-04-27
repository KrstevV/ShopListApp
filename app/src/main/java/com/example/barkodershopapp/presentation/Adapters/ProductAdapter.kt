package com.example.barkodershopapp.presentation.Adapters

import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.barkoder.shoppingApp.net.databinding.ProductListItemBinding
import com.example.barkodershopapp.OnClickListenerButtons
import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.typeconverters.TypeConverterss
import com.squareup.picasso.Picasso

class ProductAdapter (private var list : List<ProductDataEntity>, private val listener : OnClickListenerButtons): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


    class ViewHolder(private var binding : ProductListItemBinding, private val listener: OnClickListenerButtons) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list : ProductDataEntity) {
            binding.btnAddSize.setOnClickListener {
                listener.onClickPlus(list)
            }
            binding.btnMinusSize.setOnClickListener {
                listener.onClickMinus(list)
            }
            binding.textProductName.text = list.nameProduct
            binding.textProductBarcode.text = list.barcodeProduct.toString()
            binding.textCountProduct.text = list.count.toString()
            binding.textProductPrice.text = list.priceProduct.toString()
            binding.textProductTotalPrice.text = list.totalPrice.toString()

            val byteArray = list.imageProduct?.let { TypeConverterss.toBitmap(it) }
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

    fun setNotesList(lista : List<ProductDataEntity>) {
        this.list = lista
        notifyDataSetChanged()
    }

    fun getProductInt(position : Int): ProductDataEntity {
        return list[position]
    }

}