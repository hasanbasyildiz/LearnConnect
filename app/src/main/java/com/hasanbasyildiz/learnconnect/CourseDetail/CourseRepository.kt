package com.hasanbasyildiz.learnconnect.CourseDetail

import android.content.ContentValues
import android.content.Context
import com.hasanbasyildiz.learnconnect.data.DatabaseHelper

class CourseRepository(private val context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun isCourseInserted(videoId: Int): Boolean {
        val database = dbHelper.readableDatabase
        val query = "SELECT * FROM ${DatabaseHelper.TABLE_COURSE} WHERE ${DatabaseHelper.COLUMN_VIDEO_ID} = ?"
        val cursor = database.rawQuery(query, arrayOf(videoId.toString())) // videoId toString() ile dönüştürüldü
        val isInserted = cursor.moveToFirst()
        cursor.close()
        return isInserted
    }

    fun insertCourse(userId: Int, videoId: Int, imageUrl: String, videoUrl: String, courseTitle: String, isLike: Int, isSub: Int): Boolean {
        val database = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put(DatabaseHelper.COLUMN_USER_ID, userId)
            put(DatabaseHelper.COLUMN_VIDEO_ID, videoId)
            put(DatabaseHelper.COLUMN_IMAGE_URL, imageUrl)
            put(DatabaseHelper.COLUMN_VIDEO_URL, videoUrl)
            put(DatabaseHelper.COLUMN_COURSE_TITLE, courseTitle)
            put(DatabaseHelper.COLUMN_IS_LIKE, isLike)
            put(DatabaseHelper.COLUMN_IS_SUB, isSub)
        }
        return database.insert(DatabaseHelper.TABLE_COURSE, null, contentValues) != -1L
    }
}