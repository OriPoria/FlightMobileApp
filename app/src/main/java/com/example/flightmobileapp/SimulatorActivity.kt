package com.example.flightmobileapp

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.flightmobileapp.network.SimulatorApiService
import com.example.flightmobileapp.network.SimulatorProperty
import com.example.flightmobileapp.network.connectServer
import com.example.flightmobileapp.overview.SimulatorViewModel
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_simulator.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.ResponseBody
import java.lang.Math.abs
import java.lang.Math.toRadians

class SimulatorActivity : AppCompatActivity() {
    lateinit var ThrottleSeekbar: SeekBar
    lateinit var valueOfThrottleSeekBar: TextView
    lateinit var RudderSeekbar: SeekBar
    lateinit var valueOfRudderSeekBar: TextView
    var simulatorProperty = SimulatorProperty(0.toDouble(), 0.toDouble(), 0.toDouble(), 0.toDouble())

    lateinit var vm:SimulatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulator)
        vm = SimulatorViewModel(connectServer(intent.getStringExtra("url")))
        setObserver(vm)



        ThrottleSeekbar = findViewById(R.id.ThrottleseekBar) as SeekBar
        valueOfThrottleSeekBar = findViewById(R.id.ThrottleValuetextView) as TextView
        RudderSeekbar = findViewById(R.id.RudderseekBar) as SeekBar
        valueOfRudderSeekBar = findViewById(R.id.RudderValueTextView) as TextView


        RudderSeekbar.max = 100
        RudderSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                valueOfRudderSeekBar.text = progress.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

        ThrottleSeekbar.setSaveEnabled(false)
        ThrottleSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                valueOfThrottleSeekBar.text = progress.toString()
                Log.i("throttle", progress.toString())
                simulatorProperty.throttle = progress/100.toDouble()
                vm.sendCmd(simulatorProperty)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

/*
        val handler = Handler()
        //add this task to the handler loop every 2 seconds to update the view
        //at the end of the task we re-add the task to the queue to work endlessly
        try {

            handler.postDelayed(object : Runnable {
                override fun run() {
                    vm.getSimulatorImg()
                    handler.postDelayed(this, 2000)
                }
            }, 2000)

        } catch (e :Exception) {
            Log.i("err", e.message)
        }

 */
        val joystick: JoystickView = joystickView_right
        joystick.setOnMoveListener { angle, strength ->
            val rad = toRadians(angle + 0.0)

            var tempelevator = simulatorProperty.elevator
            var tempaileron = simulatorProperty.aileron

            simulatorProperty.aileron = kotlin.math.cos(rad)
            simulatorProperty.elevator = kotlin.math.sin(rad)
            simulatorProperty.aileron = (simulatorProperty.aileron * strength) / 100
            simulatorProperty.elevator = (simulatorProperty.elevator * strength) / 100

            if (abs(tempelevator - simulatorProperty.elevator) >= 0.1 ||
                abs(tempaileron - simulatorProperty.aileron) >= 0.1
            ) {
                // need to check if value in sliders changed more then 1%

                //sendValuesToServer
                vm.sendCmd(simulatorProperty)
            }

        }
    }
    val handler = Handler()
    var getImgRunnable:Runnable=object:Runnable {
        override fun run() {
            vm.getSimulatorImg()
            handler.postDelayed(this, 2000)
        }
    }

    override fun onStart() {
        super.onStart()

        //add this task to the handler loop every 2 seconds to update the view
        //at the end of the task we re-add the task to the queue to work endlessly
        try {

            handler.postDelayed(getImgRunnable, 2000)

        } catch (e :Exception) {
            Log.i("err", e.message)
        }
    }

    override fun onStop() {
       super.onStop()
        handler.removeCallbacks(getImgRunnable)

    }

    //good
    override fun onPause() {
        super.onPause()

    }


    fun setObserver(vm:SimulatorViewModel) {
        val responseImg = vm.response
        //set the image component as an observer to the changes of the response from the VM
        responseImg.observe(this, Observer<ResponseBody> { r: ResponseBody? ->
            val B = BitmapFactory.decodeStream(r?.byteStream())
            simImg.setImageBitmap(B)
        })
    }
}





