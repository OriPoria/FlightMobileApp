package com.example.flightmobileapp.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flightmobileapp.network.SimulatorApiService
import com.example.flightmobileapp.network.SimulatorProperty
//import com.example.flightmobileapp.network.retrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.ResponseBody


class SimulatorViewModel {

    // The internal MutableLiveData Img that stores the most recent response
    private val _response = MutableLiveData<ResponseBody>()

    // The external immutable LiveData for the response image
    val response: LiveData<ResponseBody>
        get() = _response

    private var viewModelJob = Job()

    //var simulatorApiService:MutableLiveData<SimulatorApiService> = MutableLiveData()
    lateinit var myService: SimulatorApiService

    //Create a coroutine scope for that new job using the main dispatcher
    val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main )


    constructor(s: SimulatorApiService) {
        myService = s
       testGetImg()
    }
    fun getSimulatorImg() {
        coroutineScope.launch {
            //Calling getSimulator() from the SimulatorApi service creates and starts the network call on a background thread,
            // returning the Deferred object for that task.
            val getImgDeferred = myService.getImg()
            try {
                val imgResult = getImgDeferred.await()
                _response.value = (imgResult)

            } catch (e: Exception) {

            }

        }


    }

    fun testGetImg() {
        coroutineScope.launch {
            val getImgDeferred = myService.getImg()
            val imgResult = getImgDeferred.await()
            _response.value = (imgResult)
        }
    }

    //send a post request to the server
    fun sendCmd(simInfo:SimulatorProperty){
        coroutineScope.launch {
            try {
                val msgReturned = myService.sendCommand(simInfo).await()
                Log.i("msg", "post succeed")
                Log.i("msg", msgReturned.toString())

                //In case there was a bad request, and the post did'nt succeed
            } catch (e: Exception) {
                Log.i("err message", e.message.toString())
            }

        }

    }


/*
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
 */
}