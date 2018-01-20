package com.yeet.notified

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import com.yeet.notified.Models.NotificationReceived
import com.yeet.notified.Models.NotificationRemoved

/**
 * Created by drakemorin on 2018-01-20.
 */
class Utility {
    companion object {
        fun getAppName(packageName: String, applicationContext: Context): String {
            val packageManager =  applicationContext.packageManager
            return packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString()
        }

        fun getAppIcon(packageName: String, applicationContext: Context): Drawable {
            val packageManager =  applicationContext.packageManager
            return packageManager.getApplicationIcon(packageName)
        }

        fun createNotificationReceived(intent: Intent, appName: String): NotificationReceived {
            val packageName = intent.getStringExtra("packageName")
            val postTime = intent.getLongExtra("postTime", -1) // TODO: Guard against negative post times
            val dayOfTheWeek = intent.getIntExtra("dayOfWeek", 6)
            return NotificationReceived(packageName, postTime, appName, dayOfTheWeek)
        }
        fun createNotificationRemoved(intent: Intent, appName: String): NotificationRemoved {
            val packageName = intent.getStringExtra("packageName")
            val postTime = intent.getLongExtra("postTime", -1) // TODO: Guard against negative post times
            val dayOfTheWeek = intent.getIntExtra("dayOfWeek", 6)
            return NotificationRemoved(packageName, postTime, appName, dayOfTheWeek)
        }
    }
}