package com.example.shoppingtiger


import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
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
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.shoppingtiger.ui.theme.ShoppingTigerTheme
import com.example.shoppingtiger.database.room.StoreItem
import com.example.shoppingtiger.database.room.StoreItemsRepo
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.mapbox.geojson.Point
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class StoresListActivity : ComponentActivity() {

    lateinit var geoClient: GeofencingClient
    var reqCode: Int = 0
    lateinit var viewModel: StoresListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = StoresListViewModel(application)
        setContent {
            ShoppingTigerTheme {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ), 1
                    )
                }
                val locationManager: LocationManager =
                    getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val currLocation =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StoresListItems(
                        viewModel = viewModel,
                        currLocation = currLocation
                    )
                }
            }
        }
        val context = this
        geoClient = LocationServices.getGeofencingClient(this)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(context, "Permissions are missing.", Toast.LENGTH_SHORT).show()
        }
        CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
            StoreItemsRepo.instance()!!.allItems.collect() { itemsFromDatabase ->

                for (item in itemsFromDatabase) {
                    var radius = item.radius.toFloat();
                    if (radius < 1.0)
                        radius = 1.0F
                    val geofence = Geofence.Builder()
                        .setCircularRegion(
                            item.lat,
                            item.long,
                            radius
                        )
                        .setRequestId("geo${++reqCode}")
                        .setExpirationDuration(60 * 60 * 1000)
                        .setTransitionTypes(
                            Geofence.GEOFENCE_TRANSITION_ENTER or
                                    Geofence.GEOFENCE_TRANSITION_EXIT
                        )
                        .build()

                    val request = GeofencingRequest.Builder()
                        .addGeofence(geofence)
                        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                        .build()
                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        reqCode,
                        Intent(context, GeofenceBroadcastReceiver::class.java),
                        PendingIntent.FLAG_MUTABLE
                    )
                    geoClient.addGeofences(request, pendingIntent)
                        .addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "Geofence: $reqCode added successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Geofence: $reqCode ERROR! status: ${it.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                }
            }
        }
    }

    @Composable
    fun StoresListItems(
        viewModel: StoresListViewModel,
        currLocation: Location?
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
                    "⇦ Stores List", fontSize = 28.sp,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Blue,
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                items(listItems.toList()) { item ->

                    val editedName = remember { mutableStateOf(item.name) }
                    val editedDescr = remember { mutableStateOf(item.description) }
                    val editedRadius = remember { mutableStateOf(item.radius.toString()) }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp, 0.dp)
                            .background(Color.White)
                    ) {

                        //name
                        var nameModifier = Modifier
                            .padding(4.dp, 8.dp, 4.dp, 0.dp)
                            .fillMaxWidth()
                            .weight(1f)

                        BasicTextField(
                            modifier = nameModifier,
                            value = editedName.value,
                            onValueChange = {
                                editedName.value = it
                                val itemCopy = item.copy(name = it)
                                viewModel.updatetItem(itemCopy)
                            },
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 20.sp)
                        )

                        //edit location
                        IconButton(
                            onClick = {
                                val intent = Intent(context, EditStoreActivity::class.java)
                                intent.putExtra("STORE_ID", item.id)
                                intent.putExtra("STORE_LONG", item.long)
                                intent.putExtra("STORE_LAT", item.lat)
                                context.startActivity(intent)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "set location"
                            )
                        }

                        //remove button
                        IconButton(
                            onClick = {
                                viewModel.deleteItem(item.id)
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove")
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp, 0.dp)
                            .background(Color.White)
                    ) {

                        //description
                        var descrModifier = Modifier
                            .padding(4.dp, 0.dp)
                            .fillMaxWidth()
                            .weight(1f)

                        BasicTextField(
                            modifier = descrModifier,
                            value = editedDescr.value,
                            onValueChange = {
                                editedDescr.value = it
                                val itemCopy = item.copy(description = it)
                                viewModel.updatetItem(itemCopy)
                            },
                            singleLine = false,
                            textStyle = TextStyle(fontSize = 12.sp)
                        )
                    }



                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp, 0.dp)
                            .background(Color.White)
                    ) {


                        Text(
                            modifier = Modifier.padding(4.dp, 0.dp),
                            text = "Radius: ",
                            fontSize = 10.sp
                        )
                        BasicTextField(
                            modifier = Modifier
                                .padding(4.dp, 0.dp, 4.dp, 4.dp)
                                .requiredWidth(24.dp),
                            value = editedRadius.value,
                            onValueChange = {
                                editedRadius.value = it
                                val itemCopy = item.copy(radius = it.toDoubleOrNull() ?: 0.0)
                                viewModel.updatetItem(itemCopy)
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 10.sp)
                        )
                    }
                    Divider()
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .clickable {
                                val storeId: Long = viewModel.insertItem(
                                    StoreItem(
                                        name = "< new store >",
                                        description = "no description",
                                        long = currLocation?.longitude ?: 21.017532,
                                        lat = currLocation?.latitude ?: 52.237049
                                    )
                                )
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
}