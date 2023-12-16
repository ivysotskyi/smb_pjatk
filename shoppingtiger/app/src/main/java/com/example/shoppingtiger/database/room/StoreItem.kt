package com.example.shoppingtiger.database.room

data class StoreItem (
    var id : Long,
    val name : String,
    val description : String,
    val place : Long = 0,
    val radius : Double = 0.0
) {
}