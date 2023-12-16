package com.example.shoppingtiger.ui.theme

import com.example.shoppingtiger.database.room.Item
import com.example.shoppingtiger.database.room.ItemDAO
import com.example.shoppingtiger.database.room.StoreItem
import com.example.shoppingtiger.database.room.StoreItemDAO

class StoreItemsRepo(private val storeItemDao: StoreItemDAO) {

    val allItems = storeItemDao.getStoreItems()
    suspend fun insert(item: StoreItem) = storeItemDao.insertItem(item)
    suspend fun update(item: StoreItem) = storeItemDao.updateItem(item)
    suspend fun delete(item: StoreItem) = storeItemDao.deleteItem(item)
    suspend fun delete(id: Long) = storeItemDao.deleteItem(id)
    suspend fun getItem(id: Long) = storeItemDao.getItem(id)
}