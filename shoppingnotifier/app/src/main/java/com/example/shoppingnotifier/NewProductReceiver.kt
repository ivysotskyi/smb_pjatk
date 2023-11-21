package com.example.shoppingnotifier

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NewProductReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.SHOPPING_ITEM_ADDED") {
            val itemId = intent.getLongExtra("ITEM_ID", -11)
            context.startService(
                Intent(context, NewProductService::class.java).also {
                    it.putExtra("ITEM_ID", itemId)
                }
            )
        }
    }
}