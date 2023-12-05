package com.example.shoppingtiger.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//@Database (
//    entities = [Item::class], version = 1
//)
//abstract class ShoppingDatabase : RoomDatabase() {
//    abstract fun itemDao():ItemDAO
//
//    companion object{
//        private var DBInstance: ShoppingDatabase? = null
//
//        fun instance(context: Context): ShoppingDatabase{
//            if(DBInstance == null){
//                DBInstance = Room.databaseBuilder(
//                    context,
//                    ShoppingDatabase::class.java,
//                    "shopping_db"
//                ).build()
//            }
//            return DBInstance as ShoppingDatabase
//        }
//    }
//}