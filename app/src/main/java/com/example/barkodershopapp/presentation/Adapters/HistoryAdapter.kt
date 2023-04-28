package com.example.barkodershopapp.presentation.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.barkoder.shoppingApp.net.databinding.HistorylistItemBinding
import com.example.barkodershopapp.data.room.HistoryDataEntity
import com.example.barkodershopapp.presentation.OnClickListener

class HistoryAdapter (private var list : List<HistoryDataEntity>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {


        class ViewHolder(private val binding : HistorylistItemBinding) : RecyclerView.ViewHolder(binding.root) {

                fun bind(list : HistoryDataEntity) {
                        binding.textListName.text = list.listName
                        binding.textCreatedDateList.text = list.listDate

                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val binding = HistorylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.bind(list[position])
        }

        fun setNotesList(lista : List<HistoryDataEntity>) {
                this.list = lista
                notifyDataSetChanged()

        }
        fun getHistoryInt(position : Int): HistoryDataEntity {
                return list[position]
        }



}