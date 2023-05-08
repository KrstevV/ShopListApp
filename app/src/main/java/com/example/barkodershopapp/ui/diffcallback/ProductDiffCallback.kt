package com.example.barkodershopapp.ui.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.example.barkodershopapp.data.db.listdatabase.ListDataEntity
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity

class ProductDiffCallback(private val oldList: List<ListDataEntity>,
                          private val newList: List<ListDataEntity>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      return  when {
          oldList[oldItemPosition].id != newList[newItemPosition].id -> {
              return false
          }
          oldList[oldItemPosition].listProducts.nameProduct != newList[newItemPosition].listProducts.nameProduct -> {
              return false
          }
          oldList[oldItemPosition].listProducts.checkout != newList[newItemPosition].listProducts.checkout -> {
              return false
          }
          oldList[oldItemPosition].listProducts.barcodeProduct != newList[newItemPosition].listProducts.barcodeProduct -> {
              return false
          }
          oldList[oldItemPosition].listProducts.imageProduct != newList[newItemPosition].listProducts.imageProduct -> {
              return false
          }
          oldList[oldItemPosition].listProducts.count != newList[newItemPosition].listProducts.count -> {
              return false
          }
          oldList[oldItemPosition].listProducts.priceProduct != newList[newItemPosition].listProducts.priceProduct -> {
              return false
          }
          oldList[oldItemPosition].listProducts.totalPrice != newList[newItemPosition].listProducts.totalPrice -> {
              return false
          }
          oldList[oldItemPosition].listProducts.priceHistory != newList[newItemPosition].listProducts.priceHistory -> {
              return false
          }
          else -> true
      }
    }

}
