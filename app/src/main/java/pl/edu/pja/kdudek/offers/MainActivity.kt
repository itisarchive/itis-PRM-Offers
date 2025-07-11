package pl.edu.pja.kdudek.offers

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.plugin.gestures.OnMapClickListener
import pl.edu.pja.kdudek.offers.ui.theme.OffersTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OffersTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val viewPortState = rememberMapViewportState {
                        setCameraOptions {
                            zoom(15.0)
                            center(Point.fromLngLat(20.9915079, 52.2238106))
                            pitch(0.0)
                            bearing(0.0)
                        }
                    }

                    var point by remember { mutableStateOf<Point?>(null) }

                    val ctx = LocalContext.current
                    MapboxMap(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        onMapClickListener = OnMapClickListener {
                            val goefence = Geofence.Builder()
                                .setCircularRegion(it.latitude(), it.longitude(), 500f)
                                .setRequestId("asdf")
                                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                                .build()
                            val req = GeofencingRequest.Builder()
                                .addGeofences(listOf(goefence))
                                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL)
                                .build()
                            val pi = PendingIntent.getBroadcast(
                                ctx,
                                1,
                                Intent(ctx, NotificationBroadcastReceiver::class.java),
                                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                            )

                            LocationServices.getGeofencingClient(ctx)
                                .addGeofences(req, pi)
                                .addOnSuccessListener {
                                    Toast.makeText(ctx, "Geofence added", Toast.LENGTH_SHORT).show()
                                }




                            point = it
                            true
                        },
                        mapViewportState = viewPortState,
                    ) {
                        val marker = rememberIconImage(R.drawable.ic_launcher_foreground)
                        point?.let {
                            PointAnnotation(
                                point = it
                            ) {
                                iconImage = marker
                                textField = "PJATK"
                                textSize = 20.0
                                iconSize = 0.1
                                interactionsState.onClicked {
                                    Toast.makeText(
                                        this@MainActivity, "Clicked", Toast.LENGTH_SHORT
                                    ).show()
                                    true
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OffersTheme {
        Greeting("Android")
    }
}