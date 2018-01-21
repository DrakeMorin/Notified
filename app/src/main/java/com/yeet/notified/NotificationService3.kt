package com.yeet.notified

import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.support.v4.content.LocalBroadcastManager
import java.util.*


class NotificationService3 : NotificationListenerService() {

    private lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        // Disregard any ongoing notifications, like Spotify if enabled (experimental)
        val sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val ignoreOngoing = sharedPref?.getBoolean("ignore_ongoing", false) ?: false
        if (ignoreOngoing && sbn?.isOngoing == true) return
        val packageName = sbn?.packageName
        val postTime = sbn?.postTime
        val dayOfTheWeek = Date(postTime!!).day


        val intent = Intent("NotifiedNotificationReceived")
        intent.putExtra("packageName", packageName)
        intent.putExtra("postTime", postTime)
        intent.putExtra("dayOfWeek", dayOfTheWeek)

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }


    override fun onNotificationRemoved(sbn: StatusBarNotification?, rankingMap: RankingMap?, reason: Int) {
        super.onNotificationRemoved(sbn, rankingMap, reason)
        // Disregard any ongoing notifications, like Spotify if enabled (experimental)
        val sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val ignoreOngoing = sharedPref?.getBoolean("ignore_ongoing", false) ?: false
        if (ignoreOngoing && sbn?.isOngoing == true) return
        val packageName = sbn?.packageName
        val postTime = sbn?.postTime
        val dayOfTheWeek = Date(postTime!!).day


        val intent = Intent("NotifiedNotificationRemoved")
        intent.putExtra("packageName", packageName)
        intent.putExtra("postTime", postTime)
        intent.putExtra("removalReason", reason)
        intent.putExtra("dayOfWeek", dayOfTheWeek)

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }


}