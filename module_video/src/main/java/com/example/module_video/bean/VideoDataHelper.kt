package com.example.module_video.bean

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Android Studio.
 * User: 身为行
 * Date: 2026/1/19
 * Time: 10.00
 * Describe: 数据库操作类
 */

class VideoDataHelper(val context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "VideoDatabase.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // 创建表
        val CREATE_TABLE = """CREATE TABLE VIDEOFEEDS (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            url TEXT,
            liked INTEGER,
            collected INTEGER
        )"""
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 数据库版本更新逻辑
        db.execSQL("DROP TABLE IF EXISTS VIDEOFEEDS")
        onCreate(db)
    }

    fun insertVideoFeed(url: String, liked: Boolean, collected: Boolean) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("url", url)
            put("liked", liked)
            put("collected", collected)
        }
        db.insert("VIDEOFEEDS", null, values)
        db.close()
    }

    fun getVideoFeed(url: String): VideoFeed {
        var videoFeed = VideoFeed(url, false, false)
        val db = this.readableDatabase
        val cursor = db.query(
            "VIDEOFEEDS",
            arrayOf("url", "liked", "collected"),
            "url=?",
            arrayOf(url),
            null,
            null,
            null
        )
        if(cursor.moveToFirst()){
            videoFeed.url = cursor.getString(0)
            videoFeed.liked = cursor.getInt(1) == 1
            videoFeed.collected = cursor.getInt(2) == 1
        }
        cursor.close()
        return videoFeed
    }
}