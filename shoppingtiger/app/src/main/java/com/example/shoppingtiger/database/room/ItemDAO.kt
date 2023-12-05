package com.example.shoppingtiger.database.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

//@Dao
//interface ItemDAO {
//    @Query("SELECT * FROM items")
//    fun getItems(): Flow<List<Item>>
//
//    @Query("SELECT * FROM items WHERE item_id=:id")
//    fun getItem(id:Int): Flow<Item>
//
//    @Insert
//    suspend fun insertItem(item:Item) : Long
//
//    @Update
//    suspend fun updateItem(item:Item)
//
//    @Delete
//    suspend fun deleteItem(item:Item)
//
//    @Query("DELETE FROM ITEMS WHERE item_id = :id")
//    suspend fun deleteItem(id:Long)
//}