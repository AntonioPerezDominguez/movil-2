package net.ivanvega.milocationymapascompose.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker



import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline

import com.google.maps.android.compose.rememberCameraPositionState

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import net.ivanvega.milocationymapascompose.permission.ui.PermissionBox

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@SuppressLint("MissingPermission")
@Composable
fun MiMapa(){
    val permissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    PermissionBox(
        permissions = permissions,
        requiredPermissions = listOf(permissions.first()),
        onGranted = {
            MiMapaContent(it.contains(Manifest.permission.ACCESS_FINE_LOCATION))
        }
    )
}

@RequiresPermission(
    anyOf = [
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    ]
)
@Composable
fun MiMapaContent(usePreciseLocation: Boolean) {
    var myCurrentLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var mapClick by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var polilyne by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    var solicitudRuta by remember { mutableIntStateOf(0) }
    var context = LocalContext.current
    var uiSettings by remember { mutableStateOf(MapUiSettings()) }
    var properties by remember { mutableStateOf(MapProperties(mapType = MapType.SATELLITE)) }

    val home = LatLng(20.14732764505376, -101.16664805744956)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(home, 10f)
    }

    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings,
            onMapClick = {
                mapClick= LatLng(it.latitude,it.longitude)
            }
        ) {

            Polyline(points = polilyne)
            Marker(
                state = MarkerState(position = home),
                title = "Home",
                snippet = "Marker in Home"
            )
            // Agregar marcador en myCurrentLocation
            if (myCurrentLocation != LatLng(0.0, 0.0)) {
                Marker(
                    state = MarkerState(position = myCurrentLocation),
                    title = "Current Location",
                    snippet = "Marker in Current Location"
                )
            }
        }

        Column(){
            val scope = rememberCoroutineScope()
            val locationClient = remember {
                LocationServices.getFusedLocationProviderClient(context)
            }
            var locationInfo by remember {
                mutableStateOf("")
            }
            Button(
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        val priority = if (usePreciseLocation) {
                            Priority.PRIORITY_HIGH_ACCURACY
                        } else {
                            Priority.PRIORITY_BALANCED_POWER_ACCURACY
                        }
                        val result = locationClient.getCurrentLocation(
                            priority,
                            CancellationTokenSource().token,
                        ).await()
                        result?.let { fetchedLocation ->
                            locationInfo =
                                "Current location is \n" + "lat : ${fetchedLocation.latitude}\n" +
                                        "long : ${fetchedLocation.longitude}\n" + "fetched at ${System.currentTimeMillis()}"
                            myCurrentLocation= LatLng(fetchedLocation.latitude,fetchedLocation.longitude)
                        }
                    }
                },
            ) {
                Text(text = "Get current location")
            }
            Button(
                onClick = {
                    solicitudRuta=1
                }
            ){
                Text("Calcular Ruta")
            }

        }
        Switch(
            checked = uiSettings.zoomControlsEnabled,
            onCheckedChange = {
                uiSettings = uiSettings.copy(zoomControlsEnabled = it)
            }
        )
    }
    if (solicitudRuta == 1) {
        trazarRuta(home, myCurrentLocation) { puntos ->
            // Actualizar el estado mutable con los puntos de la ruta
            polilyne = puntos

        }
    }

    // Dibujar la polilínea si hay puntos de ruta disponibles
    if (polilyne.isNotEmpty()) {
        Log.d("estro","polyline")
       // Polyline(points = polilyne, color = Color.Blue)
    }
}

fun trazarRuta(start: LatLng, end: LatLng, callback: (List<LatLng>) -> Unit ) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openrouteservice.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    CoroutineScope(Dispatchers.IO).launch {
        val response = retrofit.create(ApiService::class.java)
            .getRoute(
                "5b3ce3597851110001cf624835540f0fc8a346cca4945f41c38bef67",
                "${start.longitude},${start.latitude}",
                "${end.longitude},${end.latitude}"
            )
        if (response.isSuccessful) {
            var puntosRuta = calcularPuntos(response.body())
            callback(puntosRuta)
            puntosRuta.forEach{
                Log.d("${it}","antonio")
            }

        } else {
            Log.d("No se pudo", "antonio")
        }
    }
}

fun calcularPuntos(ruta: RouteResponse?): List<LatLng> {
    val puntos = mutableListOf<LatLng>()
    ruta?.features?.firstOrNull()?.geometry?.coordinates?.forEach {
        val punto = LatLng(it[1], it[0])
        puntos.add(punto)
    }
    Log.d("${puntos}","antonio")
    return puntos
}
