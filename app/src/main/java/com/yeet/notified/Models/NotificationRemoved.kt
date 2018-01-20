package com.yeet.notified.Models

import java.time.DayOfWeek

/**
 * Created by jacobsteves on 2018-01-20.
 */
class NotificationRemoved(
        val key: String,
        val packageName: String,
        val postTime: Long,
        val tickerText: String,
        val title: String,
        val text: String,
        val priority: Int,
        val category: String,
        val appName: String,
        val dayOfWeek: Int,
        val removalReason: Int)