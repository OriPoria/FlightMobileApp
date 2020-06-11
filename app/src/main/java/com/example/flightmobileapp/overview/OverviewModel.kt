package com.example.flightmobileapp.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flightmobileapp.network.SimulatorApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the most recent response
    private val _response = MutableLiveData<String>()

    // The external immutable LiveData for the response String
    val response: LiveData<String>
        get() = _response

    private var viewModelJob = Job()

    //Create a coroutine scope for that new job using the main dispatcher
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main )


    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getSimulatorProperties()
    }

    //test function to make the connection with th UI
    fun getUsers() : LiveData<String> {
        val message = MutableLiveData<String>()
        message.value = "from get users"
        return message
    }


    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getSimulatorProperties() {
        coroutineScope.launch {
            //Calling getProperties() from the MarsApi service creates and starts the network call on a background thread,
            // returning the Deferred object for that task.
            var getPropertiesDeferred = SimulatorApi.retrofitService.getProperties()
            try {
                var listResult = getPropertiesDeferred.await()

                _response.value =
                    "Success: ${listResult[0]}"

            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
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