package br.com.ceducarneiro.tuit

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TuitApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: TuitApplication? = null

        @Synchronized
        fun getInstance(): TuitApplication {
            if (instance == null) {
                TuitApplication()
            }
            return instance!!
        }
    }
}