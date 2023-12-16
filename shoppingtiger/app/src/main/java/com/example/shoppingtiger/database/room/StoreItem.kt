package com.example.shoppingtiger.database.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "storeitems")
data class StoreItem (
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id : Long,
    val name : String,
    val description : String,
    val place : Long = 0,
    val radius : Double = 0.0
)