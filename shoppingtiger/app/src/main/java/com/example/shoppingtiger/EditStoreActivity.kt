package com.example.shoppingtiger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.NoOpUpdate
import androidx.core.graphics.drawable.toBitmap
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapClickListener

class EditStoreActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap
    private lateinit var symbolManager: SymbolManager
    private var selectedSymbol: Symbol? = null
    private lateinit var permissionsManager: PermissionsManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            MapScreen()
        }


    }
}

@Composable
fun MapScreen() {
    var point: Point? by remember {
        mutableStateOf(null)
    }
    var relaunch by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val permissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (!permissions.values.all { it }) {
                //handle permission denied
            }
            else {
                relaunch = !relaunch
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        MapBoxMap(
            onPointChange = { point = it },
            point = point,
            modifier = Modifier
                .fillMaxSize()
        )
    }

//    LaunchedEffect(key1 = relaunch) {
//        try {
//            val location = LocationService().getCurrentLocation(context)
//            point = Point.fromLngLat(location.longitude, location.latitude)
//
//        } catch (e: LocationService.LocationServiceException) {
//            when (e) {
//                is LocationService.LocationServiceException.LocationDisabledException -> {
//                    //handle location disabled, show dialog or a snack-bar to enable location
//                }
//
//                is LocationService.LocationServiceException.MissingPermissionException -> {
//                    permissionRequest.launch(
//                        arrayOf(
//                            android.Manifest.permission.ACCESS_FINE_LOCATION,
//                            android.Manifest.permission.ACCESS_COARSE_LOCATION
//                        )
//                    )
//                }
//
//                is LocationService.LocationServiceException.NoNetworkEnabledException -> {
//                    //handle no network enabled, show dialog or a snack-bar to enable network
//                }
//
//                is LocationService.LocationServiceException.UnknownException -> {
//                    //handle unknown exception
//                }
//            }
//        }
//    }
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
                        .flyTo(CameraOptions.Builder().zoom(16.0).center(point).build())
                }
            }
            NoOpUpdate
        },
        modifier = modifier
    )
}
