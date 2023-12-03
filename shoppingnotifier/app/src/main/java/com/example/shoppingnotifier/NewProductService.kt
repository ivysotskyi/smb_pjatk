package com.example.shoppingnotifier

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NewProductService : Service() {

    companion object {
        var index: Int = 0
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val itemId = intent.getLongExtra("ITEM_ID", -11)

        val mainActivityIntent = Intent("com.example.SHOPPING_ITEM_ADDED_NOTIF").also{
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            it.apply {
                component = ComponentName(
                    "com.example.shoppingtiger",
                    "com.example.shoppingtiger.ProductListActivity"
                )
            }
        }

        mainActivityIntent.putExtra("ITEM_ID", itemId)

        val pendingIntent =
            PendingIntent.getActivity(
                application,
                119911,
                mainActivityIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        val notification = NotificationCompat.Builder(application, "newProductAdd")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("A new product was added.")
            .setContentText("Tap to edit it...")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Missing notification permission.", Toast.LENGTH_SHORT).show()
        }

        //startActivity(mainActivityIntent)

        NotificationManagerCompat.from(application)
            .notify(index++, notification)


        return super.onStartCommand(intent, flags, startId)
    }
}