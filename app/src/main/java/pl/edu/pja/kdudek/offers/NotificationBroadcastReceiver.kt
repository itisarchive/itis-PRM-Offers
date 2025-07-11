package pl.edu.pja.kdudek.offers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.GeofencingEvent

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val geofence = GeofencingEvent.fromIntent(intent ?: return) ?: return
        val id = geofence.triggeringGeofences?.first()?.requestId ?: return
        if (id == "asdf") {
            println("NotificationBroadcastReceiver: Received geofence event for ID: $id")
        }
    }
}