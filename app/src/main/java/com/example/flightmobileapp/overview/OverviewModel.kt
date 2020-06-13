package com.example.flightmobileapp.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flightmobileapp.network.SimulatorApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.ResponseBody



class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData Img that stores the most recent response
    private val _response = MutableLiveData<ResponseBody>()

    // The external immutable LiveData for the response image
    val response: LiveData<ResponseBody>
        get() = _response

    private var viewModelJob = Job()

    //Create a coroutine scope for that new job using the main dispatcher
    val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main )


    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getSimulatorImg()
    }



    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    fun getSimulatorImg() {

        coroutineScope.launch {
            //Calling getSimulator() from the SimulatorApi service creates and starts the network call on a background thread,
            // returning the Deferred object for that task.
            var getImgDeferred = SimulatorApi.retrofitService.getImg()
            try {
                var imgResult = getImgDeferred.await()
                _response.value = (imgResult)

            } catch (e: Exception) {

            }

        }
    }

    companion object {

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}