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
import java.net.SocketTimeoutException


class SimulatorViewModel {

    // The internal MutableLiveData Img that stores the most recent response
    private val _response = MutableLiveData<ResponseBody>()
    private val _isErr = MutableLiveData<Boolean>()
    // The external immutable LiveData for the response image
    val response: LiveData<ResponseBody> get() = _response

    // The external immutable LiveData if an error occurred
    val err:LiveData<Boolean> get() = _isErr

    var errMsg:String=""

    private var viewModelJob = Job()

    //var simulatorApiService:MutableLiveData<SimulatorApiService> = MutableLiveData()
    var myService: SimulatorApiService

    //Create a coroutine scope for that new job using the main dispatcher
    val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    constructor(s: SimulatorApiService) {
        myService = s
    }
    fun getSimulatorImg() {
        coroutineScope.launch {
        //Calling getSimulator() from the SimulatorApi service creates and starts the network call on a background thread,
        // returning the Deferred object for that task.
        val getImgDeferred = myService.getImg()
        try {
            val imgResult = getImgDeferred.await()
            _response.value = (imgResult)
            _isErr.value = false

        } catch (e: Exception) {
            if (e.localizedMessage.toString().startsWith("timeout")) {
                errMsg = "Connecion with the server problem: Timeout of get image"
            } else {errMsg="Connecion with the server problem. Try to re-connect from the main menu"}
            _isErr.value = true
        }

        }
    }


    //send a post request to the server
    fun sendCmd(simInfo:SimulatorProperty){
        coroutineScope.launch {
            try {
                val msgReturned = myService.sendCommand(simInfo).await()
                Log.i("msg", "post succeed")
                _isErr.value = false

            //In case there was a bad request, and the post did'nt succeed
            } catch (e: Exception) {
                if (e.message.toString().startsWith("timeout")) {
                    errMsg = "Connecion with the server problem: Timeout of send values"
                } else {errMsg="Connecion with the server problem. Try to re-connect from the main menu"}
                _isErr.value = true
            }

        }

    }

}