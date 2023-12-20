package com.example.shoppingtiger.database.room

import android.content.Context
import androidx.room.Room

class StoreItemsRepo(private val storeItemDao: StoreItemDAO) {

    companion object {
        private var storeItemsInstance: StoreItemsRepo? = null

        fun instance(storeItemDao: StoreItemDAO? = null): StoreItemsRepo? {
            if (storeItemsInstance == null) {
                if (storeItemDao != null) {
                    storeItemsInstance = StoreItemsRepo(storeItemDao)
                    return storeItemsInstance
                }
                return null
            }
            return storeItemsInstance
        }
    }

    val allItems = storeItemDao.getStoreItems()
    suspend fun insert(item: StoreItem) = storeItemDao.insertItem(item)
    suspend fun update(item: StoreItem) = storeItemDao.updateItem(item)
    suspend fun delete(item: StoreItem) = storeItemDao.deleteItem(item)
    suspend fun delete(id: Long) = storeItemDao.deleteItem(id)
    fun getItem(id: Long) = storeItemDao.getItem(id)
}