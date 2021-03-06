package com.yeet.notified

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric


class MainActivity : AppCompatActivity() {

    private lateinit var navBarGeneral: ImageButton
    private lateinit var navBarPopular: ImageButton
    private lateinit var navBarSettings: ImageButton
    private var navBarCurrent: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotificationReceived, IntentFilter("NotifiedNotificationReceived"))
        navBarGeneral = findViewById(R.id.nav_bar_general)
        navBarPopular = findViewById(R.id.nav_bar_popular)
        navBarSettings = findViewById(R.id.nav_bar_settings)
        if (savedInstanceState != null) navBarCurrent = savedInstanceState.getInt("NavBarCurrent", 0)
        setUpNavBar()
        openNavFragment()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle?) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState?.putInt("NavBarCurrent", navBarCurrent)
    }

    private fun setUpNavBar() {
        colourNavBar(navBarCurrent)
        navBarGeneral.setOnClickListener({
            resetNavBar()
            navBarCurrent = 0
            colourNavBar(navBarCurrent)
            openNavFragment()
        })
        navBarPopular.setOnClickListener({
            resetNavBar()
            navBarCurrent = 1
            colourNavBar(navBarCurrent)
            openNavFragment()
        })
        navBarSettings.setOnClickListener({
            resetNavBar()
            navBarCurrent = 2
            colourNavBar(navBarCurrent)
            openNavFragment()
        })
    }

    private fun colourNavBar(item: Int) {
        when (item) {
            0 -> navBarGeneral.setImageDrawable(getDrawable(R.drawable.ic_general_blue))
            1 -> navBarPopular.setImageDrawable(getDrawable(R.drawable.ic_popular_blue))
            2 -> navBarSettings.setImageDrawable(getDrawable(R.drawable.ic_settings_blue))
        }
    }

    private fun resetNavBar() {
        navBarGeneral.setImageDrawable(getDrawable(R.drawable.ic_general_black))
        navBarPopular.setImageDrawable(getDrawable(R.drawable.ic_popular_black))
        navBarSettings.setImageDrawable(getDrawable(R.drawable.ic_settings_black))
    }

    private fun openNavFragment() {
        val fragmentManager = supportFragmentManager
        val ft = fragmentManager.beginTransaction()
        when (navBarCurrent) {
            0 -> {
                val fragment = GeneralDataFragment.newInstance()
                ft.replace(R.id.container, fragment)
                ft.commit()
            }
            1 -> {
                val fragment = PerAppDataFragment()
                ft.replace(R.id.container, fragment)
                ft.commit()
            }
            2 -> {
                val fragment = SettingsFragment()
                ft.replace(R.id.container, fragment)
                ft.commit()
            }
        }


    }

    private val onNotificationReceived = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val appName = Utility.getAppName(intent.getStringExtra("packageName"), applicationContext)
            val notificationReceived = Utility.createNotificationReceived(intent, appName)
            val dbHandler = DBHandler(context)
            dbHandler.insertNotificationReceived(notificationReceived)
        }
    }
}
