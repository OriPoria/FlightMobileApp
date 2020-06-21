package com.example.flightmobileapp.network

import com.squareup.moshi.Json

data class SimulatorProperty(
    var aileron: Double,
    val rudder: Double,
    var elevator: Double,
    var throttle: Double

)