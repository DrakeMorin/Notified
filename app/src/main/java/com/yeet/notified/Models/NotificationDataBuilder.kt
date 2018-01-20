package com.yeet.notified.Models

import android.content.Intent

/**
 * Created by drakemorin on 2018-01-20.
 */
class NotificationDataBuilder {
    companion object {
        fun createNotificationReceived(intent: Intent): NotificationReceived {
            val key = intent.getStringExtra("key")
            val packageName = intent.getStringExtra("packageName")
            val postTime = intent.getLongExtra("postTime", -1) // TODO: Guard against negative post times
            val tickerText = intent.getStringExtra("tickerText")
            val title = intent.getStringExtra("title")
            val text = intent.getStringExtra("text")
            val priority = intent.getIntExtra("priority", 0)
            val category = intent.getStringExtra("category")
            return NotificationReceived(key, packageName, postTime, tickerText, title, text, priority, category)
        }
        fun createNotificationRemoved(intent: Intent): NotificationRemoved {
            val key = intent.getStringExtra("key")
            val packageName = intent.getStringExtra("packageName")
            val postTime = intent.getLongExtra("postTime", -1) // TODO: Guard against negative post times
            val tickerText = intent.getStringExtra("tickerText")
            val title = intent.getStringExtra("title")
            val text = intent.getStringExtra("text")
            val priority = intent.getIntExtra("priority", -1)
            val category = intent.getStringExtra("category")
            val removalReason = intent.getIntExtra("removalReason", -1)
            return NotificationRemoved(key, packageName, postTime, tickerText, title, text, priority, category, removalReason)
        }
    }
}