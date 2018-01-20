package com.yeet.notified.Models

import java.time.DayOfWeek

/**
 * Created by jacobsteves on 2018-01-20.
 */
class NotificationRemoved(
        val packageName: String,
        val postTime: Long,
        val appName: String,
        val dayOfWeek: Int)