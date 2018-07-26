package com.example.taras.reminerapp

import android.app.Application
import com.facebook.stetho.Stetho
import timber.log.Timber

/**
 * Created by Taras Koloshmatin on 24.07.2018
 */
class App : Application() {

    companion object AppInstance {
        private var sInstance: App? = null
        fun getInstance(): App? {
            return sInstance
        }
    }

    fun App() {
        sInstance = this
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
        } else {
            Timber.plant(NoLoggingTree())
        }
    }

    inner class NoLoggingTree : Timber.Tree() {

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {}
    }
}