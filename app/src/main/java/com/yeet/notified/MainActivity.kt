package com.yeet.notified

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, IntentFilter("Msg"));

    }

    private val onNotice = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val pack = intent.getStringExtra("package")
            val title = intent.getStringExtra("title")
            val text = intent.getStringExtra("text")


        }
    }
}
