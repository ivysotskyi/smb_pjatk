package com.example.shoppingtiger

import OptionsManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class OptionsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        OptionsManager.initialize(applicationContext)
        setContent {
            val currencyState = remember { mutableStateOf(OptionsManager.getCurrency()) }
            val showCheckboxState = remember { mutableStateOf(OptionsManager.getShowCheckbox()) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val context = LocalContext.current
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .clickable {
                            context.startActivity(Intent(context, MainActivity::class.java))
                        }
                        .padding(1.dp, 0.dp, 1.dp, 5.dp)
                ) {
                    Text(
                        "⇦ Options", fontSize = 28.sp,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Blue,
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.requiredHeight(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        text = "Currency",
                        fontSize = 24.sp
                    )
                    BasicTextField(
                        value = currencyState.value,
                        onValueChange = {
                            OptionsManager.setCurrency(it)
                            currencyState.value = it
                        },
                        maxLines = 1,
                        modifier = Modifier
                            .requiredWidth(30.dp),
                        textStyle = TextStyle(fontSize = 26.sp)
                    )
                }

                Spacer(modifier = Modifier.requiredHeight(16.dp))

                // Show Purchase Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Show Purchase Checkbox",
                        fontSize = 24.sp
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    Switch(
                        checked = showCheckboxState.value,
                        onCheckedChange = {
                            OptionsManager.setShowCheckbox(it)
                            showCheckboxState.value = it
                        }
                    )
                }
            }
        }
    }
}