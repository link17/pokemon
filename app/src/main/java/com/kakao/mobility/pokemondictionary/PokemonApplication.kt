package com.kakao.mobility.pokemondictionary

import android.app.Application
import timber.log.Timber

class PokemonApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        takeIf {BuildConfig.DEBUG }.run {
            Timber.plant(Timber.DebugTree())
        }
    }

}