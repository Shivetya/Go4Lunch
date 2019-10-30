package com.gt.go4lunch

import android.app.Application

class MainApplication: Application() {

    companion object{
        private lateinit var instanceApp: MainApplication

        @JvmStatic
        fun getInstance(): Application {
            return instanceApp
        }

        private fun setApplication(mainApplication: MainApplication){
            instanceApp = mainApplication
        }
    }

    override fun onCreate() {

        super.onCreate()

        setApplication(this)

    }
}