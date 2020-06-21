package com.example.flightmobileapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.flightmobileapp.network.SimulatorApiService
import com.example.flightmobileapp.network.connectServer
import com.example.flightmobileapp.overview.SimulatorViewModel

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.lang.Exception


class MainActivity : AppCompatActivity() {


    private val uiSocpe = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        btn.setOnClickListener {
            val intent = Intent(this, SimulatorActivity::class.java)
            startActivity(intent)

        }


        connectBtn.setOnClickListener {


            try {
                var simulatorApiService: SimulatorApiService = connectServer(url.text.toString())
                var viewModel: SimulatorViewModel = SimulatorViewModel(simulatorApiService)
                val intent = Intent(this, SimulatorActivity::class.java)
                intent.putExtra("url", url.text.toString())
                startActivity(intent)

            } catch (e: Exception) {
                //errMsg.text = "Connection failure"
                //errMsg.visibility = View.VISIBLE
            }

        }


    }
}
