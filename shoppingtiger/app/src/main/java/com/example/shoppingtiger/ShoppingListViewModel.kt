package com.example.shoppingtiger

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingtiger.database.room.Item
//import com.example.shoppingtiger.database.room.ShoppingDatabase
import com.example.shoppingtiger.ui.theme.ItemsRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShoppingListViewModel(private val app: Application) : AndroidViewModel(app) {

    private val itemRepo: ItemsRepository
    private val firebaseDatabase: FirebaseDatabase
    //val items: Flow<List<Item>>
    val items: StateFlow<HashMap<String, Item>>

    init{
        //val itemDao = ShoppingDatabase.instance(app).itemDao()

        firebaseDatabase = FirebaseDatabase.getInstance("https://shoppinglist-8e9e2-default-rtdb.europe-west1.firebasedatabase.app")
        itemRepo = ItemsRepository(firebaseDatabase)
        items = itemRepo.allItems

        //insertItem(Item(name = "Beer", quantity = 2, price = 5.5, purchased = false))
        //insertItem(Item(name = "Chocolate", quantity = 3, price = 3.0, purchased = true))

    }

    fun insertItem(item: Item, context: Context? = null){
        viewModelScope.launch {
            val insertedItemID = itemRepo.insert(item)
            context?.let {
                val intent = Intent("com.example.SHOPPING_ITEM_ADDED")
                intent.putExtra("ITEM_ID", insertedItemID)
                it.sendBroadcast(intent)
            }
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
    fun deleteItem(id: String){
        viewModelScope.launch {
            itemRepo.delete(id)
        }
    }

}