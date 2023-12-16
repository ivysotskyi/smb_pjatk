package com.example.shoppingtiger.database.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreItemDAO {

    @Query("SELECT * FROM storeitems")
    fun getStoreItems(): Flow<List<StoreItem>>

    @Query("SELECT * FROM storeitems WHERE id=:id")
    fun getItem(id:Long): Flow<StoreItem>

    @Insert
    suspend fun insertItem(item:StoreItem) : Long

    @Update
    suspend fun updateItem(item:StoreItem)

    @Delete
    suspend fun deleteItem(item:StoreItem)

    @Query("DELETE FROM storeitems WHERE id = :id")
    suspend fun deleteItem(id:Long)
}