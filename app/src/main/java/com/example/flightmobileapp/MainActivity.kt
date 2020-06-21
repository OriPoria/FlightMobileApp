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
import com.example.flightmobileapp.overview.User
import com.example.flightmobileapp.overview.UsersDataBase
import com.example.flightmobileapp.overview.UsersDataDao
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception


class MainActivity : AppCompatActivity() {

 //   private var dbJob = Job()
    private val uiSocpe = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        btn.setOnClickListener {
            val intent = Intent(this, SimulatorActivity::class.java)
            startActivity(intent)

        }
        val db: UsersDataBase = UsersDataBase.getInstance(applicationContext)

        connectBtn.setOnClickListener {
            val newUser = User(url.text.toString())
            uiSocpe.launch {
                try {
                    db.userDatabaseDao.insert(newUser)

                } catch (e : Exception) {
                    //err if key is already in the data bsae TODO how to handle it...
                    Log.i("msg", e.message.toString())
                }
            }
            try {
                var simulatorApiService: SimulatorApiService = connectServer(url.text.toString())
                var viewModel : SimulatorViewModel= SimulatorViewModel(simulatorApiService)
                val intent = Intent(this, SimulatorActivity::class.java)
                intent.putExtra("url" ,url.text.toString())
                startActivity(intent)

            } catch (e:Exception) {
                errMsg.text = "Connection failure"
                errMsg.visibility = View.VISIBLE
            }

        }

        //val context: Context = applicationContex

        uiSocpe.launch {
            try {
                val users:List<User> = db.userDatabaseDao.getTable()
                txt1.text = users[0].url
                txt2.text = users[1].url
                txt3.text = users[2].url
                txt4.text = users[3].url
                txt5.text = users[4].url

            } catch (e:Exception) {}


        }

    }

}

/*
fun View.toggleVisibility() {
    if (visibility == View.VISIBLE) {
        visibility = View.INVISIBLE
    } else {
        visibility = View.VISIBLE
    }
}
 */