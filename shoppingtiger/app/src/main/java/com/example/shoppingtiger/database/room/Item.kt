package com.example.shoppingtiger.database.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Item (
    var id : String = "",
    val name : String,
    val price : Double = 0.0,
    val quantity : Long = 0,
    val purchased : Boolean = false
) {
}