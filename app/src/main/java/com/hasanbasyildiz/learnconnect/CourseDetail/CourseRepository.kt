package com.hasanbasyildiz.learnconnect.CourseDetail

import android.content.ContentValues
import android.content.Context
import com.hasanbasyildiz.learnconnect.data.DatabaseHelper

class CourseRepository(private val context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun isCourseInserted(userId: Int, videoId: Int): Boolean {
        val database = dbHelper.readableDatabase
        val query = "SELECT * FROM ${DatabaseHelper.TABLE_COURSE} WHERE ${DatabaseHelper.COLUMN_USER_ID} = ? AND ${DatabaseHelper.COLUMN_VIDEO_ID} = ?"
        val cursor = database.rawQuery(query, arrayOf(userId.toString(), videoId.toString()))
        val isInserted = cursor.moveToFirst()
        cursor.close()
        return isInserted
    }

    fun insertOrUpdateCourse(
        userId: Int,
        videoId: Int,
        imageUrl: String,
        videoUrl: String,
        courseTitle: String,
        isLike: Int? = null,
        isSub: Int? = null
    ): Boolean {
        val database = dbHelper.writableDatabase
        val isInserted = isCourseInserted(userId, videoId)
        return if (isInserted) {
            val contentValues = ContentValues().apply {
                put(DatabaseHelper.COLUMN_IMAGE_URL, imageUrl)
                put(DatabaseHelper.COLUMN_VIDEO_URL, videoUrl)
                put(DatabaseHelper.COLUMN_COURSE_TITLE, courseTitle)
                put(DatabaseHelper.COLUMN_IS_LIKE, isLike)
                put(DatabaseHelper.COLUMN_IS_SUB, isSub)
            }
            database.update(
                DatabaseHelper.TABLE_COURSE,
                contentValues,
                "${DatabaseHelper.COLUMN_USER_ID} = ? AND ${DatabaseHelper.COLUMN_VIDEO_ID} = ?",
                arrayOf(userId.toString(), videoId.toString())
            ) > 0
        } else {
            val contentValues = ContentValues().apply {
                put(DatabaseHelper.COLUMN_USER_ID, userId)
                put(DatabaseHelper.COLUMN_VIDEO_ID, videoId)
                put(DatabaseHelper.COLUMN_IMAGE_URL, imageUrl)
                put(DatabaseHelper.COLUMN_VIDEO_URL, videoUrl)
                put(DatabaseHelper.COLUMN_COURSE_TITLE, courseTitle)
                put(DatabaseHelper.COLUMN_IS_LIKE, isLike)
                put(DatabaseHelper.COLUMN_IS_SUB, isSub)
            }
            database.insert(DatabaseHelper.TABLE_COURSE, null, contentValues) != -1L
        }
    }

    fun updateIsLike(userId: Int, videoId: Int, isLike: Int): Boolean {
        val database = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put(DatabaseHelper.COLUMN_IS_LIKE, isLike)
        }
        return database.update(
            DatabaseHelper.TABLE_COURSE,
            contentValues,
            "${DatabaseHelper.COLUMN_USER_ID} = ? AND ${DatabaseHelper.COLUMN_VIDEO_ID} = ?",
            arrayOf(userId.toString(), videoId.toString())
        ) > 0
    }
}