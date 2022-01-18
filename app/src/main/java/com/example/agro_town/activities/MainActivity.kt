package com.example.agro_town.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.agro_town.R
import com.example.agro_town.models.Constans

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences(Constans.AGROTOWNPREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constans.LOGGED_IN_USERNAME, "")
        val tvMain = findViewById<TextView>(R.id.tv_main)
        tvMain.text= "The Logged in user is $username."




    }
}