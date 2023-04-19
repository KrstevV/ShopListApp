package com.example.barkoder.presentation.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.barkoder.shoppingApp.net.databinding.HistorylistItemBinding
import com.example.barkoder.OnClickListenerButtons
import com.example.barkoder.data.room.HistoryDataEntity
import com.example.barkoder.presentation.OnClickListener

class HistoryAdapter (private var list : ArrayList<HistoryDataEntity>, var listener : OnClickListener): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {


        class ViewHolder(private val binding : HistorylistItemBinding, var listener: OnClickListener) : RecyclerView.ViewHolder(binding.root) {

                @SuppressLint("SuspiciousIndentation")
                fun bind(list : HistoryDataEntity) {
                     binding.textListName.text = list.listName
                     binding.textCreatedDateList.text = list.listCreated
                        binding.recViewHistoryList.setOnClickListener {
                                listener.onClick(list)
                        }

                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val binding = HistorylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding, listener)
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.bind(list[position])
        }

        fun setNotesList(lista : ArrayList<HistoryDataEntity>) {
                this.list = lista
                notifyDataSetChanged()

        }
        fun getHistoryInt(position : Int): HistoryDataEntity {
                return list[position]
        }



}