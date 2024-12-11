package com.hasanbasyildiz.learnconnect.myCourses

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hasanbasyildiz.learnconnect.Module.CoursesSql


class MyCoursesViewModel(application: Application) : AndroidViewModel(application) {

    private val database: SQLiteDatabase by lazy {
        object : SQLiteOpenHelper(application, "learnconnect.db", null, 1) {
            override fun onCreate(db: SQLiteDatabase?) {}
            override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
        }.writableDatabase
    }

    private val _courses = MutableLiveData<List<CoursesSql>>()
    val courses: LiveData<List<CoursesSql>> get() = _courses

    fun fetchCourses(context: Context) {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)

        if (userId == -1) {
            _courses.postValue(emptyList())
            return
        }

        val courses = mutableListOf<CoursesSql>()
        val cursor = database.query(
            "course",
            null,
            "is_sub = ? AND user_id = ?",
            arrayOf("1", userId.toString()),
            null,
            null,
            null
        )
        cursor?.use {
            while (it.moveToNext()) {
                courses.add(
                    CoursesSql(
                        userId = it.getInt(it.getColumnIndexOrThrow("user_id")),
                        videoId = it.getInt(it.getColumnIndexOrThrow("video_id")),
                        imageUrl = it.getString(it.getColumnIndexOrThrow("image_url")),
                        videoUrl = it.getString(it.getColumnIndexOrThrow("video_url")),
                        courseTitle = it.getString(it.getColumnIndexOrThrow("course_title")),
                        isLike = it.getInt(it.getColumnIndexOrThrow("is_like")),
                        isSub = it.getInt(it.getColumnIndexOrThrow("is_sub"))
                    )
                )
            }
        }
        _courses.postValue(courses)
    }
}


