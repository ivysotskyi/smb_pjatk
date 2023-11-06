package com.example.shoppingtiger.database.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "items")
data class Item (
    @ColumnInfo(name = "item_id")
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val name : String,
    val price : Double = 0.0,
    val quantity : Int = 0,
    val purchased : Boolean = false,
) {
}