package com.example.flightmobileapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
        //val context: Context = applicationContext
        val db: UsersDataBase = UsersDataBase.getInstance(applicationContext)

        uiSocpe.launch {
            txt1.text = db.userDatabaseDao.getTable()[0].url
            txt2.text = db.userDatabaseDao.getTable()[1].url
            txt3.text = db.userDatabaseDao.getTable()[2].url
            txt4.text = db.userDatabaseDao.getTable()[3].url
            txt5.text = db.userDatabaseDao.getTable()[4].url



        }

    }

}