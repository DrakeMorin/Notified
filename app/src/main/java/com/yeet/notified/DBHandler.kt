package com.yeet.notified

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private val DATABASE_VERSION = 1
private val DATABASE_NAME = "Notified.db"

class DBHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    private val TABLE_NOTIFICATION_RECEIVED = "notification_received"
    private val TABLE_NOTIFICATION_REMOVED = "notification_removed"
    private val TABLE_SMS_NOTIFICATION = "sms_notification"

    override fun onCreate(db: SQLiteDatabase?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // TODO: Write a proper migration switch-case statement
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION_RECEIVED)
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION_REMOVED)
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_SMS_NOTIFICATION)
        onCreate(db)
    }
}