package com.example.barkodershopapp.presentation.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.barkoder.shoppingApp.net.databinding.SelectproductItemBinding
import com.example.barkodershopapp.data.room.ProductDataEntity
import com.example.barkodershopapp.presentation.OnClickSelectProduct
import com.example.barkodershopapp.typeconverters.TypeConverterss
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class SelectProductAdapter(private var list : ArrayList<ProductDataEntity>, private val listenerSelect : OnClickSelectProduct ) : RecyclerView.Adapter<SelectProductAdapter.ViewHolder>(), Filterable{

    private var productListFull: List<ProductDataEntity> = ArrayList()

    init {
        productListFull = list
    }

    fun setProductsList2(productsList: ArrayList<ProductDataEntity>) {
        productListFull = productsList
        list = productsList
        notifyDataSetChanged()
    }
            class ViewHolder(private val binding : SelectproductItemBinding, private val listenerSelect : OnClickSelectProduct) : RecyclerView.ViewHolder(binding.root) {
                fun bind (list: ProductDataEntity) {
                    binding.textSelectProductName.text = list.nameProduct
                    binding.textSelectProductBarcode.text = list.barcodeProduct
                    binding.textSelectProductPrice.text = list.priceProduct.toString()
                    binding.selectLayout.setOnClickListener {
                        listenerSelect.onClickSelect(list)
                    }
                    val byteArray = list.imageProduct?.let { TypeConverterss.toBitmap(it) }
                    binding.imageSelectProduct.load(byteArray) {
                        crossfade(true)
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



    fun setProductsList(lista : ArrayList<ProductDataEntity>) {
        this.list = lista
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<ProductDataEntity>()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(productListFull)
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    for (product in productListFull) {
                        if (product.nameProduct?.toLowerCase(Locale.ROOT)!!.contains(filterPattern)) {
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
                list.addAll(results?.values as ArrayList<ProductDataEntity>)
                notifyDataSetChanged()
            }
        }
    }



}