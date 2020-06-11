package com.example.flightmobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.flightmobileapp.overview.OverviewViewModel
import kotlinx.android.synthetic.main.activity_simulator.*

class SimulatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulator)
        val vm : OverviewViewModel by viewModels()
      //  vm.getUsers().observe(this, Observer { s ->  })
    }
}