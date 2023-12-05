package com.example.shoppingtiger.ui.theme

import com.example.shoppingtiger.database.room.Item
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow

import androidx.compose.animation.core.snap
import com.google.firebase.database.DatabaseError

class ItemsRepository(private val firebaseDatabase: FirebaseDatabase) {

    //val allItems = itemDao.getItems()
    val allItems = MutableStateFlow(HashMap<String, Item>())

    init {
        firebaseDatabase.getReference("items").addChildEventListener(object: ChildEventListener{

            private fun getItemFromStnapshot(snapshot: DataSnapshot):Item {
                return Item(
                    id = snapshot.ref.key as String,
                    name = (snapshot.child("name").value as? String)?:"",
                    price = (snapshot.child("price").value as? Long)?.toDouble()?:0.0,
                    purchased = (snapshot.child("purchased").value as? Boolean)?:false,
                    quantity = (snapshot.child("quantity").value as? Long)?:0
                )
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val item = getItemFromStnapshot(snapshot)
                allItems.value = allItems.value.toMutableMap().apply {
                    put(item.id, item)
                } as HashMap<String, Item>
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val item = getItemFromStnapshot(snapshot)
                allItems.value = allItems.value.toMutableMap().apply {
                    put(item.id, item)
                } as HashMap<String, Item>
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val item = getItemFromStnapshot(snapshot)
                allItems.value = allItems.value.toMutableMap().apply {
                    remove(item.id)
                } as HashMap<String, Item>
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun insert(item: Item): String {

        val inserted = firebaseDatabase.getReference("items").push()

        inserted.child("name").setValue(item.name)
        inserted.child("price").setValue(item.price)
        inserted.child("purchased").setValue(item.purchased)
        inserted.child("quantity").setValue(item.quantity)

        return inserted.key ?: ""
    }

    fun update(item: Item) {

        val itemToUpdate = firebaseDatabase.getReference("items/${item.id}")

        itemToUpdate.child("name").setValue(item.name)
        itemToUpdate.child("price").setValue(item.price)
        itemToUpdate.child("purchased").setValue(item.purchased)
        itemToUpdate.child("quantity").setValue(item.quantity)
    }
    fun delete(item: Item){
        delete(item.id)
    }
    fun delete(id: String){
        firebaseDatabase.getReference("items/${id}").removeValue()
    }
}