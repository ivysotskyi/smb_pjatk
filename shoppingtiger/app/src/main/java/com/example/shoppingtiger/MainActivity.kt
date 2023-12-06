package com.example.shoppingtiger

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppingtiger.ui.theme.ShoppingTigerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingTigerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize().padding(15.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val context = LocalContext.current
                        Button(
                            modifier = Modifier
                                .requiredHeight(120.dp)
                                .fillMaxWidth(),
                            onClick = {
                                context.startActivity(Intent(context, ProductListActivity::class.java))
                            }
                        )
                        {
                            Text(
                                text = "Go Shopping !",
                                fontSize = 32.sp
                            )
                        }
                        Spacer(
                            modifier = Modifier.requiredHeight(20.dp)
                        )
                        Button(
                            modifier = Modifier
                                .requiredHeight(120.dp)
                                .fillMaxWidth(),
                            onClick = {
                                context.startActivity(Intent(context, OptionsActivity::class.java))
                            }
                        )
                        {
                            Text(
                                text = "Options",
                                fontSize = 32.sp
                            )
                        }
                    }
                }
            }
        }
    }
}