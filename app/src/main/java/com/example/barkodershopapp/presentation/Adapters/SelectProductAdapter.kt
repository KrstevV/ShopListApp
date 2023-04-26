package com.example.barkodershopapp.presentation.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.barkoder.shoppingApp.net.databinding.SelectproductItemBinding
import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.domain.userdataacc.ApiData
import com.example.barkodershopapp.domain.userdataacc.Product
import com.example.barkodershopapp.presentation.OnClickSelectProduct
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class SelectProductAdapter(private var list : ArrayList<Product>, private val listenerSelect : OnClickSelectProduct ) : RecyclerView.Adapter<SelectProductAdapter.ViewHolder>(), Filterable{

    private var productListFull: List<Product> = ArrayList()

    init {
        productListFull = list
    }

    fun setProductsList2(productsList: ArrayList<Product>) {
        productListFull = productsList
        list = productsList
        notifyDataSetChanged()
    }
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



    fun setProductsList(lista : ArrayList<Product>) {
        this.list = lista
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<Product>()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(productListFull)
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    for (product in productListFull) {
                        if (product.name.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                            filteredList.add(product)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                list.clear()
                list.addAll(results?.values as ArrayList<Product>)
                notifyDataSetChanged()
            }
        }
    }



}