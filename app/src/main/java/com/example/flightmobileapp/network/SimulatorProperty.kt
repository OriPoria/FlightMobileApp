package com.example.flightmobileapp.network

import com.squareup.moshi.Json

data class SimulatorProperty(
    val id: String, @Json(name = "img_src") val imgSrcUrl: String,
    val type: String,
    val price: Double
    /*
    val aileron: Double,
    val rudder: Double,
    val elevator: Double,
    val throttle: Double

     */

)