package com.example.barkodershopapp.presentation.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.barkoder.shoppingApp.net.databinding.ActivitySelectProductBinding
import com.barkoder.shoppingApp.net.databinding.ProductListItemBinding
import com.barkoder.shoppingApp.net.databinding.SelectproductItemBinding
import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.domain.userdataacc.ApiData
import com.example.barkodershopapp.domain.userdataacc.Product
import com.example.barkodershopapp.presentation.OnClickSelectProduct
import com.squareup.picasso.Picasso

class SelectProductAdapter(private var list : List<Product>, private val listenerSelect : OnClickSelectProduct ) : RecyclerView.Adapter<SelectProductAdapter.ViewHolder>(){

            class ViewHolder(private val binding : SelectproductItemBinding, private val listenerSelect : OnClickSelectProduct) : RecyclerView.ViewHolder(binding.root) {
                fun bind (list : Product) {
                    binding.textSelectProductName.text = list.name
                    binding.textSelectProductBarcode.text = list.barcode
                    binding.textSelectProductPrice.text = list.price.toString()
                    Picasso.get().load(list.image).into(binding.imageSelectProduct)
                    binding.selectLayout.setOnClickListener {
                        listenerSelect.onClickSelect(list)
                    }
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SelectproductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, listenerSelect)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setProductsList(lista : List<Product>) {
        this.list = lista
        notifyDataSetChanged()
    }

}