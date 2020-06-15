package com.example.flightmobileapp.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

//http://10.0.2.2:6200/api
private const val BASE_URL = "http://10.0.2.2:6200/api/"


private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()



interface SimulatorApiService {
    @Headers("Content-Type: application/json")
    @POST("command")
    fun sendCommand(@Body userData: SimulatorProperty):
            Deferred<ResponseBody>

    @GET("screenshot")
    fun getImg():Deferred<ResponseBody>

}


object SimulatorApi {
    val retrofitService : SimulatorApiService by lazy {
        retrofit.create(SimulatorApiService::class.java)   }
}