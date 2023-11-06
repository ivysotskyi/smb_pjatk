package com.example.shoppingtiger

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import com.example.shoppingtiger.database.room.Item
import com.example.shoppingtiger.database.room.ShoppingDatabase
import com.example.shoppingtiger.ui.theme.ItemsRepository
class ShoppingListViewModel(private val app: Application) : AndroidViewModel(app) {

    private val itemRepo: ItemsRepository
    val items: Flow<List<Item>>

    init{
        val itemDao = ShoppingDatabase.instance(app).itemDao()
        itemRepo = ItemsRepository(itemDao)
        insertItem(Item(name = "Banana"))
        insertItem(Item(name = "Pineaple"))
        items = itemRepo.allItems
    }
    fun insertItem(item: Item){
        viewModelScope.launch {
            itemRepo.insert(item)
        }
    }
    fun updatetItem(item: Item){
        viewModelScope.launch {
            itemRepo.update(item)
        }
    }
    fun deleteItem(item: Item){
        viewModelScope.launch {
            itemRepo.delete(item)
        }
    }

}