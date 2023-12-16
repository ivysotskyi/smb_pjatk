package com.example.shoppingtiger.database.room

class StoreItemsRepo(private val storeItemDao: StoreItemDAO) {

    val allItems = storeItemDao.getStoreItems()
    suspend fun insert(item: StoreItem) = storeItemDao.insertItem(item)
    suspend fun update(item: StoreItem) = storeItemDao.updateItem(item)
    suspend fun delete(item: StoreItem) = storeItemDao.deleteItem(item)
    suspend fun delete(id: Long) = storeItemDao.deleteItem(id)
    suspend fun getItem(id: Long) = storeItemDao.getItem(id)
}