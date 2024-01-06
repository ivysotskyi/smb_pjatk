package com.example.shoppingtiger

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent != null) {
            for( geofence in geofencingEvent.triggeringGeofences!!){
                if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
                    Toast.makeText(context, "You are close to ${geofence.requestId}", Toast.LENGTH_SHORT).show()
                }else if(geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
                    Toast.makeText(context, "You left ${geofence.requestId}", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Log.e("GeoError", "GeofencingEvent is null.")
        }
    }
}