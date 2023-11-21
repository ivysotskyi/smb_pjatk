package com.example.shoppingnotifier

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NewProductReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.SHOPPING_ITEM_ADDED") {
            val itemId = intent.getLongExtra("ITEM_ID", -11)
            showNotification(itemId)
//            context.startService(
//                Intent(context, NewUserService::class.java).also {
//                    it.putExtra("rc", intent.getIntExtra("rc", 0))
//                    it.putExtra("login", intent.getStringExtra("user"))
//                }
//            )
        }
    }


    private fun showNotification(itemId: Long) {
        // Implement your logic to show a notification
        // You can use NotificationCompat.Builder for this purpose
        // Also, set up a PendingIntent to open the first application when the notification is clicked
    }
}