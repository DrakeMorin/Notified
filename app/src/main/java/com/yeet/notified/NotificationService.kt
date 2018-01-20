package com.yeet.notified

import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.support.v4.content.LocalBroadcastManager
import android.util.Log


class NotificationService : NotificationListenerService() {

    private val TAG: String = this.javaClass.simpleName
    private lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        val pack = sbn?.getPackageName()
        val ticker = sbn?.getNotification()?.tickerText.toString()
        val extras = sbn?.getNotification()?.extras
        val title = extras?.getString("android.title")
        val text = extras?.getCharSequence("android.text")!!.toString()

        Log.i("Package", pack)
        Log.i("Ticker", ticker)
        Log.i("Title", title)
        Log.i("Text", text)

        val msgrcv = Intent("Msg")
        msgrcv.putExtra("package", pack)
        msgrcv.putExtra("ticker", ticker)
        msgrcv.putExtra("title", title)
        msgrcv.putExtra("text", text)

        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        Log.i("Msg", "Notification Removed");
    }
    
}