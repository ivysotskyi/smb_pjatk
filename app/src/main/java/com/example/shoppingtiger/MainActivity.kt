package com.example.shoppingtiger

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppingtiger.ui.theme.ShoppingTigerTheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
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
    viewModel:ShoppingListViewModel,
    modifier: Modifier = Modifier) {

    val listItems by viewModel.items.collectAsState(emptyList())

    LazyColumn(
        modifier = Modifier.requiredHeight(500.dp)
    ){
        items(listItems) { item->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.White)
            ) {
                // Checkbox on the right
                Checkbox(
                    checked = item.purchased,
                    onCheckedChange = { checked ->
                        val itemCopy = item.copy(purchased = checked)
                        viewModel.updatetItem(itemCopy)
                    }
                )

                // Shopping item name
                Text(
                    text = item.name,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    fontSize = 20.sp
                )

                //quantity
                BasicTextField(
                    modifier = Modifier.padding(8.dp),
                    value = item.quantity.toString(),
                    onValueChange = { updatedQuantity ->
                        val quantity = updatedQuantity.toIntOrNull() ?: 0
                        val itemCopy = item.copy(quantity = quantity)
                        viewModel.updatetItem(itemCopy)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 18.sp)
                )

                // Price
                Text(
                    text = "ZŁ${item.price}",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 18.sp
                )
            }
            Divider()
        }
    }
}

