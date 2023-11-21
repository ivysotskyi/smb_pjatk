package com.example.shoppingnotifier

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.example.shoppingnotifier.ui.theme.ShoppingnotifierTheme

class MainActivity : ComponentActivity() {

    private val brcstReceiver = NewProductReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingnotifierTheme {
                Text(
                    text = "I will notify you about new products in a shopping list.",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        NotificationManagerCompat.from(application)
            .createNotificationChannel(NotificationChannel(
                "newProductAdd",
                "Shopping Items Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ))
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStart() {
        super.onStart()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                123//notifications permission request code
            )
        }

        registerReceiver(
            brcstReceiver,
            IntentFilter("com.example.SHOPPING_ITEM_ADDED"),
            RECEIVER_EXPORTED
        )
    }
    override fun onDestroy() {
        unregisterReceiver(brcstReceiver)
        super.onDestroy()
    }
}
