package com.yeet.notified

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.yeet.notified.Models.NotificationReceived
import com.yeet.notified.Models.NotificationRemoved


private const val DATABASE_VERSION = 4
private const val DATABASE_NAME = "Notified.db"
private const val TABLE_NOTIFICATION_RECEIVED = "notification_received"
private const val COL_ID = "id"
private const val COL_PACKAGE_NAME = "package_name"
private const val COL_POST_TIME = "post_time"
private const val COL_APP_NAME = "app_name"
private const val COL_DAY_OF_WEEK = "day_of_week"
private const val TABLE_NOTIFICATION_REMOVED = "notification_removed"
private const val TABLE_SMS_NOTIFICATION = "sms_notification"

class DBHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL("""
                CREATE TABLE $TABLE_NOTIFICATION_RECEIVED
                ($COL_ID INTEGER PRIMARY KEY,
                $COL_PACKAGE_NAME TEXT,
                $COL_POST_TIME DATETIME,
                $COL_DAY_OF_WEEK INTEGER,
                $COL_APP_NAME TEXT)
                """
        )

        db?.execSQL("""
                CREATE TABLE $TABLE_NOTIFICATION_REMOVED
                ($COL_ID INTEGER PRIMARY KEY,
                $COL_PACKAGE_NAME TEXT,
                $COL_POST_TIME DATETIME,
                $COL_DAY_OF_WEEK INTEGER,
                $COL_APP_NAME TEXT)
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

    fun clearAllTables() {
        val db = writableDatabase
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION_RECEIVED)
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION_REMOVED)
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_SMS_NOTIFICATION)
        onCreate(db)
    }

    fun insertNotificationReceived(notification: NotificationReceived) {
        val values = ContentValues()

        values.put(COL_PACKAGE_NAME, notification.packageName)
        values.put(COL_POST_TIME, notification.postTime)
        values.put(COL_APP_NAME, notification.appName)
        values.put(COL_DAY_OF_WEEK, notification.dayOfWeek)

        val db = writableDatabase

        val lon = db.insert(TABLE_NOTIFICATION_RECEIVED, null, values)
        db.close()
    }

    fun insertNotificationRemoved(notification: NotificationRemoved) {
        val values = ContentValues()

        values.put(COL_PACKAGE_NAME, notification.packageName)
        values.put(COL_POST_TIME, notification.postTime)
        values.put(COL_APP_NAME, notification.appName)
        values.put(COL_DAY_OF_WEEK, notification.dayOfWeek)

        val db = writableDatabase

        db.insert(TABLE_NOTIFICATION_RECEIVED, null, values)
        db.close()
    }

    private fun getEntryPercentFromCursor(cursor: Cursor, total: Int): ArrayList<PieEntry> {
        if (cursor.count == 0) return arrayListOf()
        val entries: ArrayList<PieEntry> = ArrayList()
        var counter = 0
        var percentSoFar = 0f
        cursor.moveToFirst()

        while(!cursor.isAfterLast && counter < 5) {
            val appName = cursor.getString(cursor.getColumnIndex(COL_APP_NAME))
            val appCount = cursor.getString(cursor.getColumnIndex("count"))
            val percentage = (java.lang.Float.parseFloat(appCount) / total) * 100

            ++counter
            percentSoFar += percentage

            entries.add(PieEntry(percentage, appName))
            cursor.moveToNext()
        }

        if (percentSoFar < 100f) {
            entries.add(PieEntry(100f - percentSoFar, "Other"))
        }

        return entries
    }

    private fun getEntryListFromCursor(cursor: Cursor): ArrayList<Entry> {
        if (cursor.count == 0) return arrayListOf()
        val entries: ArrayList<Entry> = ArrayList()
        cursor.moveToFirst()

        var sundayCount = 0f
        var mondayCount = 0f
        var tuesdayCount = 0f
        var wednesdayCount = 0f
        var thursdayCount = 0f
        var fridayCount = 0f
        var saturdayCount = 0f

        while(!cursor.isAfterLast) {
            if (cursor.getString(cursor.getColumnIndex(COL_DAY_OF_WEEK)) == "0") ++sundayCount
            if (cursor.getString(cursor.getColumnIndex(COL_DAY_OF_WEEK)) == "1") ++mondayCount
            if (cursor.getString(cursor.getColumnIndex(COL_DAY_OF_WEEK)) == "2") ++tuesdayCount
            if (cursor.getString(cursor.getColumnIndex(COL_DAY_OF_WEEK)) == "3") ++wednesdayCount
            if (cursor.getString(cursor.getColumnIndex(COL_DAY_OF_WEEK)) == "4") ++thursdayCount
            if (cursor.getString(cursor.getColumnIndex(COL_DAY_OF_WEEK)) == "5") ++fridayCount
            if (cursor.getString(cursor.getColumnIndex(COL_DAY_OF_WEEK)) == "6") ++saturdayCount
            cursor.moveToNext()
        }

        entries.add(Entry(0f, sundayCount))
        entries.add(Entry(1f, mondayCount))
        entries.add(Entry(2f, tuesdayCount))
        entries.add(Entry(3f, wednesdayCount))
        entries.add(Entry(4f, thursdayCount))
        entries.add(Entry(5f, fridayCount))
        entries.add(Entry(6f, saturdayCount))

        return entries
    }

    fun getMostPopularTime(isDay: Boolean, isAsc: Boolean, appName: String?): ArrayList<Entry> {
        val db = readableDatabase
        val where = if (appName != null) "$COL_APP_NAME = '$appName'" else null
        val cursor = db.query(TABLE_NOTIFICATION_RECEIVED, null, where, null, null, null, null)
        val entries: ArrayList<Entry> = getEntryListFromCursor(cursor)
        cursor.close()
        db.close()

        return entries
    }

    fun getPieGraph(): ArrayList<PieEntry> {
        val db = readableDatabase
        val rawQuery = "SELECT $COL_APP_NAME, COUNT($COL_APP_NAME) as count FROM $TABLE_NOTIFICATION_RECEIVED GROUP BY $COL_APP_NAME ORDER BY count DESC"
        val cursor = db.rawQuery(rawQuery, null)
        val cursorToCount = db.query(TABLE_NOTIFICATION_RECEIVED, null, null, null, null, null, null, null)
        val count = cursorToCount.count
        val entries: ArrayList<PieEntry> = getEntryPercentFromCursor(cursor, count)

        cursor.close()
        cursorToCount.close()
        db.close()
        return entries
    }

    fun getAllAppNames(): ArrayList<String> {
        val db = readableDatabase
        val rawSql = "SELECT DISTINCT $COL_APP_NAME FROM $TABLE_NOTIFICATION_RECEIVED"
        val cursor = db.rawQuery(rawSql, null)

        if (cursor.count == 0) return arrayListOf()
        cursor.moveToFirst()
        val appNames: ArrayList<String> = ArrayList()
        while(!cursor.isAfterLast) {
            appNames.add(cursor.getString(cursor.getColumnIndex(COL_APP_NAME)))
            cursor.moveToNext()
        }

        cursor.close()
        db.close()
        return appNames
    }

}