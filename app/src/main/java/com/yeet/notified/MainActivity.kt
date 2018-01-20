package com.yeet.notified

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import com.crashlytics.android.Crashlytics;
import com.yeet.notified.Models.NotificationDataBuilder
import io.fabric.sdk.android.Fabric;


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotificationReceived, IntentFilter("NotifiedNotificationReceived"));
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotificationRemoved, IntentFilter("NotifiedNotificationRemoved"));
        DBHandler(this)
    }

    private val onNotificationReceived = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val notificationReceived = Utility.createNotificationReceived(intent)
            val dbHandler = DBHandler(context)
            dbHandler.insertNotificationReceived(notificationReceived)
        }
    }

    private val onNotificationRemoved = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            var notificationRemoved = Utility.createNotificationRemoved(intent)
            val dbHandler = DBHandler(context)
            dbHandler.insertNotificationRemoved(notificationRemoved)
        }
    }
}
