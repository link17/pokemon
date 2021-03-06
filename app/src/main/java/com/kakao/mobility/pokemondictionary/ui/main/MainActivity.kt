package com.kakao.mobility.pokemondictionary.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kakao.mobility.pokemondictionary.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

}
