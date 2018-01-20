package com.yeet.notified.Models

/**
 * Created by jacobsteves on 2018-01-20.
 */
class NotifcationRemoved(
        val key: String,
        val packageName: String,
        val postTime: Long,
        val tickerText: String,
        val title: String,
        val text: String,
        val priority: Int,
        val category: String
) {
}