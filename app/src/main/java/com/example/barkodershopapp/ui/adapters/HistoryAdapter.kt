package com.example.barkodershopapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.HistorylistItemBinding
import com.example.barkodershopapp.data.db.historydatabase.HistoryDataEntity
import com.example.barkodershopapp.ui.fragments.HistoryListFragmentDirections

class HistoryAdapter(private var list: List<HistoryDataEntity>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {


    class ViewHolder(private val binding: HistorylistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(list: HistoryDataEntity) {
            binding.listNameHome2.text = list.listName
            binding.listSizeHome2.text = list.listProducts.size.toString()
            binding.listCircleText2.text = list.listName.first().toUpperCase().toString()
            binding.recViewHistoryList.setOnClickListener {
                val action = HistoryListFragmentDirections.actionHistoryListFragment2ToCurrentListFragment2(list)
                Navigation.findNavController(binding.root).navigate(action,
                    NavOptions.Builder().setPopUpTo(R.id.historyListFragment2, true).build())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            HistorylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setNotesList(lista: List<HistoryDataEntity>) {
        this.list = lista
        notifyDataSetChanged()

    }

    fun getHistoryInt(position: Int): HistoryDataEntity {
        return list[position]
    }


}