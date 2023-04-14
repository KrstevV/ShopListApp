package com.example.barkodershopapp.data.room

import android.content.Context
import com.example.barkodershopapp.data.listproductdata.ProductData
import com.example.barkodershopapp.data.listproductdata.ProductDataSource

class ProductRoomSource (context : Context) :  ProductDataSource{

    var productDaoo = ProductDatabase.getProductInstance(context).productDao()

    override suspend fun addProductList(list: ProductData)  = productDaoo.insert(ProductDataEntity.fromProduct(list))

    override suspend fun deleteProductList(list: ProductData) = productDaoo.deleteItem(ProductDataEntity.fromProduct(list))

    override suspend fun getProductList(id: Long): ProductData? = productDaoo.getItem(id)?.toProduct()

    override suspend fun getAllProductList(): List<ProductData> = productDaoo.getAll().map { it.toProduct()}
}