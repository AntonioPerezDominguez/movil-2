package com.example.mapwithmarker

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.PixelCopy.Request
import android.widget.ActionMenuView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


class MapsMarkerActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var mapa:GoogleMap
    lateinit var btnCalculate: Button
    var start:String=""
    var end:String=""
    var poly :Polyline? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)

        btnCalculate=findViewById(R.id.btnCalculateRoute)
        btnCalculate.setOnClickListener{
            start=""
            end=""
            poly?.remove()
            poly=null
            Toast.makeText(this,"Selecciona punto de origen y final", Toast.LENGTH_SHORT).show()
            if(::mapa.isInitialized){
                mapa.setOnMapClickListener {
                    if(start.isEmpty()){
                        start="${it.longitude},${it.latitude}"
                        //start="8.681495,49.41461"
                    }
                    else if(end.isEmpty()){
                        //end="8.687872,49.420318"
                        end="${it.longitude},${it.latitude}"
                        createRoute()
                    }
                }
            }
        }


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mapa = googleMap
    }

    private fun createRoute(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ApiService::class.java).getRoute("5b3ce3597851110001cf624835540f0fc8a346cca4945f41c38bef67",start,end)
            if(call.isSuccessful){
                //Log.i("antonio","ok")
                drawRoute(call.body())
            }
            else{
                Log.i("antonio","ko")
            }
        }
    }

    private fun drawRoute(routeResponse: RouteResponse?) {
        val polyLineOptions = PolylineOptions()
        routeResponse?.features?.first()?.geometry?.coordinates?.forEach {
            polyLineOptions.add(LatLng(it[1],it[0]))
        }
        runOnUiThread{
            poly=mapa.addPolyline(polyLineOptions)
        }
    }


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }





}

