package com.example.shoppingnotifier

import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
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
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStart() {
        super.onStart()
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
