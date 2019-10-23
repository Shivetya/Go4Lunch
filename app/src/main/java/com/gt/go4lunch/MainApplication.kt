package com.gt.go4lunch

import android.app.Application

class MainApplication: Application() {

    companion object{
        lateinit var instanceApp: MainApplication

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