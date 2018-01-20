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

        // Disregard any ongoing notifications, like Spotify
        //if (sbn?.isOngoing == true) return
        val key = sbn?.key
        val packageName = sbn?.packageName
        val postTime = sbn?.postTime
        val tickerText = sbn?.notification?.tickerText.toString()
        val extras = sbn?.notification?.extras
        val title = extras?.getString("android.title")
        val text = extras?.getCharSequence("android.text")?.toString()
        val priority = sbn?.notification?.priority
        val category = sbn?.notification?.category


        val intent = Intent("NotifiedNotificationReceived")
        intent.putExtra("key", key)
        intent.putExtra("packageName", packageName)
        intent.putExtra("postTime", postTime)
        intent.putExtra("tickerText", tickerText)
        intent.putExtra("title", title)
        intent.putExtra("text", text)
        intent.putExtra("priority", priority)
        intent.putExtra("category", category)

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }


    override fun onNotificationRemoved(sbn: StatusBarNotification?, rankingMap: RankingMap?, reason: Int) {
        super.onNotificationRemoved(sbn, rankingMap, reason)
        // Disregard any ongoing notifications, like Spotify
        //if (sbn?.isOngoing == true) return
        val key = sbn?.key
        val packageName = sbn?.packageName
        val postTime = sbn?.postTime
        val tickerText = sbn?.notification?.tickerText.toString()
        val extras = sbn?.notification?.extras
        val title = extras?.getString("android.title")
        val text = extras?.getCharSequence("android.text")?.toString()
        val priority = sbn?.notification?.priority
        val category = sbn?.notification?.category



        val intent = Intent("NotifiedNotificationRemoved")
        intent.putExtra("key", key)
        intent.putExtra("packageName", packageName)
        intent.putExtra("postTime", postTime)
        intent.putExtra("tickerText", tickerText)
        intent.putExtra("title", title)
        intent.putExtra("text", text)
        intent.putExtra("priority", priority)
        intent.putExtra("category", category)
        intent.putExtra("removalReason", reason)

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }


}