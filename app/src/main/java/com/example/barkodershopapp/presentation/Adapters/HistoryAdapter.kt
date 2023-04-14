package com.example.barkodershopapp.presentation.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.barkodershopapp.data.listhistorydata.HistoryListData
import com.example.barkodershopapp.databinding.HistorylistItemBinding

class HistoryAdapter (private var list : List<HistoryListData>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

        class ViewHolder(private val binding : HistorylistItemBinding) : RecyclerView.ViewHolder(binding.root) {
                fun bind(list : HistoryListData) {
                     binding.textListName.text = list.nameList
                     binding.textCreatedDateList.text = list.createdListDate
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

        fun setNotesList(lista : List<HistoryListData>) {
                this.list = lista
                notifyDataSetChanged()
        }

}