package com.example.shoppingtiger

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingtiger.database.room.ShoppingDatabase
import com.example.shoppingtiger.database.room.StoreItem
import com.example.shoppingtiger.database.room.StoreItemsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EditStoreViewModel(private val app: Application) : AndroidViewModel(app) {
    private val storeItemsRepo: StoreItemsRepo
    val items: Flow<List<StoreItem>>

    init{
        val storeItemDao = ShoppingDatabase.instance(app).storeItemDao()
        storeItemsRepo = StoreItemsRepo.instance(storeItemDao = storeItemDao)!!
        items = storeItemsRepo.allItems
    }


    fun getItemById(id: Long) = storeItemsRepo.getItem(id);

    fun updatetItem(item: StoreItem){
        viewModelScope.launch {
            storeItemsRepo.update(item)
        }
    }
}