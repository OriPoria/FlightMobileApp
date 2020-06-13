package com.example.flightmobileapp.network

import com.squareup.moshi.Json

data class SimulatorProperty(
    val aileron: Double,
    val rudder: Double,
    val elevator: Double,
    val throttle: Double

)