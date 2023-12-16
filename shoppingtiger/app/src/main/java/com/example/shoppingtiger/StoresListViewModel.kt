package com.example.shoppingtiger

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingtiger.database.room.Item
import com.example.shoppingtiger.ui.theme.ItemsRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.shoppingtiger.database.room.ShoppingDatabase
import com.example.shoppingtiger.database.room.StoreItem
import com.example.shoppingtiger.database.room.StoreItemsRepo

class StoresListViewModel(private val app: Application) : AndroidViewModel(app)  {
    private val storeItemsRepo: StoreItemsRepo
    val items: Flow<List<StoreItem>>

    init{
        val storeItemDao = ShoppingDatabase.instance(app).storeItemDao()
        storeItemsRepo = StoreItemsRepo(storeItemDao)
        viewModelScope.launch {
            storeItemsRepo.allItems.collect { itemList ->
                for (item in itemList) {
                    // Perform operations on 'item'
                    Log.i("", "Item ID: ${item.id}, Name: ${item.name}")
                }
            }
        }
        items = storeItemsRepo.allItems
    }

    fun insertItem(item: StoreItem){
        viewModelScope.launch {
            storeItemsRepo.insert(item)
        }
    }
    fun updatetItem(item: StoreItem){
        viewModelScope.launch {
            storeItemsRepo.update(item)
        }
    }
    fun deleteItem(item: StoreItem){
        viewModelScope.launch {
            storeItemsRepo.delete(item)
        }
    }
    fun deleteItem(id: Long){
        viewModelScope.launch {
            storeItemsRepo.delete(id)
        }
    }
}