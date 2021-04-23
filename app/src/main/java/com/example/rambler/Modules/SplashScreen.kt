package com.example.rambler.Modules

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rambler.Activities.Homepage
import com.example.rambler.R


class SplashScreen : Application() {
    override fun onCreate() {
        super.onCreate()

    }

    class Splashscreen : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_splashscreen)


            val background = object : Thread() {
                override fun run() {
                    try {
                        Thread.sleep(2000)
                        val intent = Intent(baseContext, Homepage::class.java)
                        startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            background.start()
        }
    }
}