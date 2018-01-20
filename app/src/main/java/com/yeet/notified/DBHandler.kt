package com.yeet.notified

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private val DATABASE_VERSION = 1
private val DATABASE_NAME = "Notified.db"

class DBHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    private val TABLE_NOTIFICATION_RECEIVED = "notification_received"
    private val COL_ID = "id"
    private val COL_KEY = "key"
    private val COL_PACKAGE_NAME = "package_name"
    private val COL_POST_TIME = "post_time"
    private val COL_TICKER_TEXT = "ticker_text"
    private val COL_TITLE = "title"
    private val COL_TEXT = "text"
    private val COL_PRIORITY = "priority"
    private val COL_CATEGORY = "category"
    private val TABLE_NOTIFICATION_REMOVED = "notification_removed"
    private val TABLE_SMS_NOTIFICATION = "sms_notification"

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL("""
                CREATE TABLE $TABLE_NOTIFICATION_RECEIVED
                ($COL_ID INTEGER PRIMARY KEY,
                $COL_KEY TEXT,
                $COL_PACKAGE_NAME TEXT,
                $COL_POST_TIME DATETIME,
                $COL_TICKER_TEXT TEXT,
                $COL_TITLE TEXT,
                $COL_TEXT TEXT,
                $COL_PRIORITY INTEGER)
                """
        )

        db?.execSQL("""
                CREATE TABLE $TABLE_NOTIFICATION_REMOVED
                ($COL_ID INTEGER PRIMARY KEY,
                $COL_KEY TEXT,
                $COL_PACKAGE_NAME TEXT,
                $COL_POST_TIME DATETIME,
                $COL_TICKER_TEXT TEXT,
                $COL_TITLE TEXT,
                $COL_TEXT TEXT,
                $COL_PRIORITY INTEGER,
                $COL_CATEGORY TEXT)
                """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // TODO: Write a proper migration switch-case statement
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION_RECEIVED)
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION_REMOVED)
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_SMS_NOTIFICATION)
        onCreate(db)
    }
}