package net.ivanvega.milocationymapascompose.ui.maps

import com.google.gson.annotations.SerializedName

data class RouteResponse(@SerializedName("features") val features:List<Features>)
data class Features(@SerializedName("geometry") val geometry:Geometry)
data class Geometry(@SerializedName("coordinates") val coordinates:List<List<Double>>)
