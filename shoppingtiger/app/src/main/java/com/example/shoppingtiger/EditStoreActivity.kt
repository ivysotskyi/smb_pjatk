package com.example.shoppingtiger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.NoOpUpdate
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewModelScope
import com.example.shoppingtiger.database.room.StoreItem
import com.example.shoppingtiger.database.room.StoreItemsRepo
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.common.location.LocationService
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.coroutineContext

class EditStoreActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap
    private lateinit var symbolManager: SymbolManager
    private var selectedSymbol: Symbol? = null
    private lateinit var permissionsManager: PermissionsManager
    var storeId: Long = -11

    private fun getCurrentStore() = GlobalScope.async {
        StoreItemsRepo.instance()!!.getItem(storeId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storeId = intent.getLongExtra("STORE_ID", -11)

        val viewModel = EditStoreViewModel(application)
        var permissionsListener: PermissionsListener = object : PermissionsListener {
            override fun onExplanationNeeded(permissionsToExplain: List<String>) {
            }

            override fun onPermissionResult(granted: Boolean) {
            }
        }

        if (!PermissionsManager.areLocationPermissionsGranted(this)) {
            permissionsManager = PermissionsManager(permissionsListener)
            permissionsManager.requestLocationPermissions(this)
        }
        setContent {
            MapScreen(viewModel = viewModel, storeId=storeId)
        }


    }
}

@Composable
fun MapScreen(viewModel: EditStoreViewModel, storeId: Long) {
    val storeItem = viewModel.getItemById(storeId).collectAsState(initial = null)

    var point: Point = remember(storeItem) {
        storeItem.value?.let {
            Point.fromLngLat(it.long, it.lat)
        } ?: Point.fromLngLat(0.0, 0.0) // Provide a default value or handle the null case
    }

    LaunchedEffect(storeItem) {
        storeItem.value?.let {
            // Update the point whenever storeItem changes
            point = Point.fromLngLat(it.long, it.lat)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        MapBoxMap(
            onPointChange = {
                //point = it
                viewModel.updatetItem(storeItem.value!!.copy(long = it.longitude(), lat = it.latitude()))
            },
            point = point,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
fun MapBoxMap(
    modifier: Modifier = Modifier,
    onPointChange: (Point) -> Unit,
    point: Point?,
) {
    val context = LocalContext.current
    val marker = remember(context) {
        context.getDrawable(R.drawable.location_icon)!!.toBitmap()
    }
    var pointAnnotationManager: PointAnnotationManager? by remember {
        mutableStateOf(null)
    }
    AndroidView(
        factory = {
            MapView(it).also { mapView ->
                mapView.getMapboxMap().loadStyleUri(Style.TRAFFIC_DAY)
                val annotationApi = mapView.annotations
                pointAnnotationManager = annotationApi.createPointAnnotationManager()

                mapView.getMapboxMap().addOnMapClickListener { p ->
                    onPointChange(p)
                    true
                }
            }
        },
        update = { mapView ->
            if (point != null) {
                pointAnnotationManager?.let {
                    it.deleteAll()
                    val pointAnnotationOptions = PointAnnotationOptions()
                        .withPoint(point)
                        .withIconImage(marker)

                    it.create(pointAnnotationOptions)
                    mapView.getMapboxMap()
                        .flyTo(CameraOptions.Builder().zoom(5.0).center(point).build())
                }
            }
            NoOpUpdate
        },
        modifier = modifier
    )
}
