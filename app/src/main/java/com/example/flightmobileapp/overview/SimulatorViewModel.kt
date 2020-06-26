package com.example.flightmobileapp.overview

import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flightmobileapp.network.SimulatorApiService
import com.example.flightmobileapp.network.SimulatorProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SimulatorViewModel constructor(s: SimulatorApiService) {

    // The internal MutableLiveData Img that stores the most recent response
    private val _response = MutableLiveData<ResponseBody>()
    private val _isErr = MutableLiveData<Boolean>()

    // The external immutable LiveData for the response image
    val response: LiveData<ResponseBody> get() = _response

    var myQueue: Queue<ResponseBody> = LinkedList<ResponseBody>()

    // The external immutable LiveData if an error occurred
    val err: LiveData<Boolean> get() = _isErr

    var errMsg: String = ""

    private var viewModelJob = Job()

    //var simulatorApiService:MutableLiveData<SimulatorApiService> = MutableLiveData()
    var myService: SimulatorApiService = s

    //Create a coroutine scope for that new job using the main dispatcher
    val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun updateImg() {
    coroutineScope.launch {
        val rs=myQueue.poll()
        if (rs!= null) {
            _response.value = rs
        } else {
            errMsg="Connection with the server problem: Image is not updated"
            _isErr.value = true
        }
        }
    }


    fun getSimulatorImg() {
        coroutineScope.launch {
        //Calling getSimulator() from the SimulatorApi service creates and starts the network call on a background thread,
        // returning the Deferred object for that task.
        val getImgDeferred = myService.getImg()
        try {
            val imgResult = getImgDeferred.await()
            myQueue.add(imgResult)

       //     _response.value = (imgResult)
            _isErr.value = false


            //Error from server handling
        } catch (e: Exception) {
            val strErr:String? = e.localizedMessage
            if (strErr != null && strErr.startsWith("timeout")) {
                errMsg = "Connection with the server problem: Timeout of get image"
            } else {errMsg="Connection with the server problem. Try to re-connect from the main menu"}
            _isErr.value = true
        }

        }
    }


    //send a post request to the server
    fun sendCmd(simInfo:SimulatorProperty){
        coroutineScope.launch {
                myService.sendCommand(simInfo).enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        errMsg = "Connection with the server problem: failure sending values"
                        _isErr.value = true
                    }
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        _isErr.value = false
                    }
                })


        }

    }

}