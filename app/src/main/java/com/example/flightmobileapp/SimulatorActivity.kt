package com.example.flightmobileapp

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.flightmobileapp.overview.OverviewViewModel
import kotlinx.android.synthetic.main.activity_simulator.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.ResponseBody

class SimulatorActivity : AppCompatActivity() {
    lateinit var mainHandler: Handler
    val vm : OverviewViewModel by viewModels()
/*
    private val updateImg = object : Runnable {
        override fun run() {
            requestImg()
            mainHandler.postDelayed(this, 1000)
        }
    }

 */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulator)

 //       mainHandler = Handler(Looper.getMainLooper())
       requestImg()
    /*
    fun somethingUsefulOneAsync() = vm.coroutineScope.async {
        while (true)
            Log.i("msg", "1!")

    }

     */


    button.setOnClickListener {
        vm.getSimulatorImg()

    }
        //get the image from the server
//        val responseImg = vm.response
  //      responseImg.observe(this, Observer<ResponseBody> {r:ResponseBody? ->
    //        val B = BitmapFactory.decodeStream(r?.byteStream())
      //      simImg.setImageBitmap(B)
        //})
    }
    fun requestImg() {
        val responseImg = vm.response
        responseImg.observe(this, Observer<ResponseBody> {r:ResponseBody? ->
            val B = BitmapFactory.decodeStream(r?.byteStream())
            simImg.setImageBitmap(B)
        })
    }


}