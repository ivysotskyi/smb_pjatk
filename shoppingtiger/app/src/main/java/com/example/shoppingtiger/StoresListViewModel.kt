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
import kotlinx.coroutines.async

class StoresListViewModel(private val app: Application) : AndroidViewModel(app)  {
    private val storeItemsRepo: StoreItemsRepo
    val items: Flow<List<StoreItem>>

    init{
        val storeItemDao = ShoppingDatabase.instance(app).storeItemDao()
        storeItemsRepo = StoreItemsRepo.instance(storeItemDao = storeItemDao)!!
        items = storeItemsRepo.allItems
    }

    fun insertItem(item: StoreItem):Long{
        var result: Long = -11 // Default value or an appropriate default for your use case
        viewModelScope.launch {
            result=storeItemsRepo.insert(item)
        }
        return result
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