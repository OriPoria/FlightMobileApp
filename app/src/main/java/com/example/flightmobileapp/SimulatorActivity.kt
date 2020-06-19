package com.example.flightmobileapp

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.flightmobileapp.network.SimulatorProperty
import com.example.flightmobileapp.overview.OverviewViewModel
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.android.synthetic.main.activity_simulator.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.ResponseBody
import java.lang.Math.abs
import java.lang.Math.toRadians

class SimulatorActivity : AppCompatActivity() {
    val vm : OverviewViewModel by viewModels()
    lateinit var ThrottleSeekbar :SeekBar
    lateinit var valueOfThrottleSeekBar :TextView
    lateinit var RudderSeekbar :SeekBar
    lateinit var valueOfRudderSeekBar :TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulator)
        setObserver()

        ThrottleSeekbar = findViewById(R.id.ThrottleseekBar) as SeekBar
        valueOfThrottleSeekBar = findViewById(R.id.ThrottleValuetextView) as TextView
        RudderSeekbar = findViewById(R.id.RudderseekBar) as SeekBar
        valueOfRudderSeekBar = findViewById(R.id.RudderValueTextView) as TextView


        RudderSeekbar.max =100
        RudderSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                valueOfRudderSeekBar.text = progress.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

        ThrottleSeekbar.max =100
        ThrottleSeekbar.setSaveEnabled(false)
        ThrottleSeekbar.setOnSeekBarChangeListener(object  : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                valueOfThrottleSeekBar.text = progress.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })



        val handler = Handler()
        //add this task to the handler loop every 2 seconds to update the view
        //at the end of the task we re-add the task to the queue to work endlessly
        handler.postDelayed(object : Runnable{
            override fun run() {
                vm.getSimulatorImg()
                handler.postDelayed(this, 2000)
            }
        },2000)


        var simulatorProperty =  SimulatorProperty(0.0,0.0,
            0.0,0.0)
        val joystick: JoystickView  = joystickView_right
        joystick.setOnMoveListener { angle, strength ->
            val rad = toRadians(angle + 0.0)

            var tempelevator= simulatorProperty.elevator
            var tempaileron = simulatorProperty.aileron

            simulatorProperty.aileron = kotlin.math.cos(rad)
            simulatorProperty.elevator = kotlin.math.sin(rad)
            simulatorProperty.aileron = (simulatorProperty.aileron * strength) / 100
            simulatorProperty.elevator = (simulatorProperty.elevator * strength) / 100

            if(abs(tempelevator - simulatorProperty.elevator) >= 0.1 ||
                abs(tempaileron - simulatorProperty.aileron) >= 0.1 ){
                // need to check if value in sliders changed more then 1%

                //sendValuesToServer
                vm.sendCmd(simulatorProperty)
            }

        }
    }


    fun setObserver() {
        val responseImg = vm.response
        //set the image component as an observer to the changes of the response from the VM
        responseImg.observe(this, Observer<ResponseBody> {r:ResponseBody? ->
            val B = BitmapFactory.decodeStream(r?.byteStream())
            simImg.setImageBitmap(B)
        })
    }









}