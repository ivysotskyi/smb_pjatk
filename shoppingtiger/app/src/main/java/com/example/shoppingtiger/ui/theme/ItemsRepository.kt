package com.example.shoppingtiger.ui.theme

import com.example.shoppingtiger.database.room.Item
import com.example.shoppingtiger.database.room.ItemDAO

class ItemsRepository(private val itemDao: ItemDAO) {

    val allItems = itemDao.getItems()
    suspend fun insert(item: Item) = itemDao.insertItem(item)
    suspend fun update(item: Item) = itemDao.updateItem(item)
    suspend fun delete(item: Item) = itemDao.deleteItem(item)
    suspend fun delete(id: Long) = itemDao.deleteItem(id)
    suspend fun getItem(id: Int) = itemDao.getItem(id)
}