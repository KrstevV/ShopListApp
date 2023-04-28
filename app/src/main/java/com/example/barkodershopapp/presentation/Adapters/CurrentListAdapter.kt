package com.example.barkodershopapp.presentation.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.barkoder.shoppingApp.net.databinding.CurrentlistListBinding
import com.barkoder.shoppingApp.net.databinding.HistorylistItemBinding
import com.example.barkodershopapp.data.room.HistoryDataEntity
import com.example.barkodershopapp.data.room.ListDataEntity
import com.example.barkodershopapp.presentation.HistoryListFragment
import com.example.barkodershopapp.presentation.HistoryListFragmentDirections
import com.example.barkodershopapp.presentation.SelectProductFragmentDirections
import com.example.barkodershopapp.typeconverters.TypeConverterss

class CurrentListAdapter(private var list : List<ListDataEntity>) : RecyclerView.Adapter<CurrentListAdapter.ViewHolder>() {

    class ViewHolder(private val binding : CurrentlistListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(list : ListDataEntity) {
            binding.textProductName.text = list.listProducts.nameProduct
            binding.textProductPrice.text = list.listProducts.priceProduct.toString()
            binding.textProductBarcode.text = list.listProducts.barcodeProduct
            val byteArray = list.listProducts.imageProduct?.let { TypeConverterss.toBitmap(it) }
            binding.imageProduct.load(byteArray) {
                crossfade(true)
            }



        }
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrentListAdapter.ViewHolder {
        val binding = CurrentlistListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrentListAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size


    fun setNotesList(lista : List<ListDataEntity>) {
        this.list = lista
        notifyDataSetChanged()

    }
    fun getHistoryInt(position : Int): ListDataEntity {
        return list[position]
    }



}
