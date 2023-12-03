package com.example.shoppingtiger

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class OpenProductReceiver  : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "com.example.SHOPPING_ITEM_ADDED_NOTIF") {
            val itemId = intent.getLongExtra("ITEM_ID", -11)
        }
    }
}