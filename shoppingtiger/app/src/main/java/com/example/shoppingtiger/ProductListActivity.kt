package com.example.shoppingtiger

import OptionsManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppingtiger.database.room.Item
import com.example.shoppingtiger.ui.theme.ShoppingTigerTheme

class ProductListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingTigerTheme {
                val viewModel = ShoppingListViewModel(application)
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShoppingListItems(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun ShoppingListItems(
    viewModel: ShoppingListViewModel
) {
    val listItems by viewModel.items.collectAsState(emptyList())
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
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
                "⇦ Products List", fontSize = 28.sp,
                modifier = Modifier.fillMaxWidth(),
                color = Color.Blue,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            items(listItems) { item ->

                val editedQuantity = remember { mutableStateOf(item.quantity.toString()) }
                val editedPrice = remember { mutableStateOf(item.price.toString()) }
                val editedName = remember { mutableStateOf(item.name) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .background(Color.White)
                ) {
                    // Checkbox on the right
                    if (OptionsManager.getShowCheckbox())
                        Checkbox(
                            checked = item.purchased,
                            onCheckedChange = { checked ->
                                val itemCopy = item.copy(purchased = checked)
                                viewModel.updatetItem(itemCopy)
                            }
                        )

                    //name
                    BasicTextField(
                        modifier = Modifier
                            .padding(4.dp, 8.dp)
                            .fillMaxWidth()
                            .weight(1f),
                        value = editedName.value,
                        onValueChange = {
                            editedName.value = it
                            val itemCopy = item.copy(name = it)
                            viewModel.updatetItem(itemCopy)
                        },
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 20.sp)
                    )

                    //quantity
                    BasicTextField(
                        modifier = Modifier
                            .padding(4.dp, 8.dp)
                            .requiredWidth(20.dp),
                        value = editedQuantity.value,
                        onValueChange = {
                            editedQuantity.value = it
                            val itemCopy = item.copy(quantity = it.toIntOrNull() ?: 0)
                            viewModel.updatetItem(itemCopy)
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 18.sp)
                    )

                    // Price
                    Text(
                        modifier = Modifier.padding(10.dp, 8.dp, 1.dp, 8.dp),
                        text = OptionsManager.getCurrency(),
                        fontSize = 18.sp
                    )
                    BasicTextField(
                        modifier = Modifier
                            .padding(4.dp, 8.dp)
                            .requiredWidth(36.dp),
                        value = editedPrice.value,
                        onValueChange = {
                            editedPrice.value = it
                            val itemCopy = item.copy(price = it.toDoubleOrNull() ?: 0.0)
                            viewModel.updatetItem(itemCopy)
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 18.sp)
                    )

                    //remove button
                    IconButton(
                        onClick = {
                            viewModel.deleteItem(item.id)
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove")
                    }
                }
                Divider()
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .clickable {
                            viewModel.insertItem(Item(name = "< new item >", quantity = 1), context = context)
                        }
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("+ Add New", fontSize = 20.sp, color = Color.Blue)
                }
            }
        }
    }
}

