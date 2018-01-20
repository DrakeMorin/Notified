package com.yeet.notified

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageButton
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


class MainActivity : AppCompatActivity() {

    private lateinit var navBarGeneral: ImageButton
    private lateinit var navBarPopular: ImageButton
    private lateinit var navBarSms: ImageButton
    private lateinit var navBarSettings: ImageButton
    private var navBarCurrent: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotificationReceived, IntentFilter("NotifiedNotificationReceived"));
        //LocalBroadcastManager.getInstance(this).registerReceiver(onNotificationRemoved, IntentFilter("NotifiedNotificationRemoved"));
        navBarGeneral = findViewById(R.id.nav_bar_general)
        navBarPopular = findViewById(R.id.nav_bar_popular)
        navBarSms = findViewById(R.id.nav_bar_sms)
        navBarSettings = findViewById(R.id.nav_bar_settings)
        if (savedInstanceState != null) navBarCurrent = savedInstanceState.getInt("NavBarCurrent", 0)
        setUpNavBar()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle?) {
        super.onSaveInstanceState(savedInstanceState)
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState?.putInt("NavBarCurrent", navBarCurrent)
    }

    private fun setUpNavBar() {
        colourNavBar(navBarCurrent)
        navBarGeneral.setOnClickListener({
            resetNavBar()
            navBarCurrent = 0
            colourNavBar(navBarCurrent)
        })
        navBarPopular.setOnClickListener({
            resetNavBar()
            navBarCurrent = 1
            colourNavBar(navBarCurrent)
        })
        navBarSms.setOnClickListener({
            resetNavBar()
            navBarCurrent = 2
            colourNavBar(navBarCurrent)
        })
        navBarSettings.setOnClickListener({
            resetNavBar()
            navBarCurrent = 3
            colourNavBar(navBarCurrent)
        })
    }

    private fun colourNavBar(item: Int) {
        if (item == 0) {
            navBarGeneral.setImageDrawable(getDrawable(R.drawable.ic_general_blue))
        } else if (item == 1) {
            navBarPopular.setImageDrawable(getDrawable(R.drawable.ic_popular_blue))
        } else if (item == 2) {
            navBarSms.setImageDrawable(getDrawable(R.drawable.ic_sms_blue))
        } else if (item == 3) {
            navBarSettings.setImageDrawable(getDrawable(R.drawable.ic_settings_blue))
        }
    }

    private fun resetNavBar() {
        navBarGeneral.setImageDrawable(getDrawable(R.drawable.ic_general_black))
        navBarPopular.setImageDrawable(getDrawable(R.drawable.ic_popular_black))
        navBarSms.setImageDrawable(getDrawable(R.drawable.ic_sms_black))
        navBarSettings.setImageDrawable(getDrawable(R.drawable.ic_settings_black))
    }

    private val onNotificationReceived = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            Log.d("FUCK", "HERE WE AREEEEE!!!!!")
            val appName = Utility.getAppName(intent.getStringExtra("packageName"), applicationContext)
            val notificationReceived = Utility.createNotificationReceived(intent, appName)
            val dbHandler = DBHandler(context)
            dbHandler.insertNotificationReceived(notificationReceived)
        }
    }

    private val onNotificationRemoved = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val appName = Utility.getAppName(intent.getStringExtra("packageName"), applicationContext)
            val notificationRemoved = Utility.createNotificationRemoved(intent, appName)
            val dbHandler = DBHandler(context)
            dbHandler.insertNotificationRemoved(notificationRemoved)
        }
    }
}
