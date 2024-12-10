package com.hasanbasyildiz.learnconnect.wishlist

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hasanbasyildiz.learnconnect.Module.CoursesSql
import com.hasanbasyildiz.learnconnect.data.DatabaseHelper.Companion.TABLE_COURSE

class WishlistViewModel(application: Application) : AndroidViewModel(application) {

    private val database: SQLiteDatabase by lazy {
        object : SQLiteOpenHelper(application, "learnconnect.db", null, 1) {
            override fun onCreate(db: SQLiteDatabase?) {}
            override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
        }.writableDatabase
    }

    private val _wishlist = MutableLiveData<List<CoursesSql>>()
    val wishlist: LiveData<List<CoursesSql>> get() = _wishlist

    private val sharedPreferences = application.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    fun loadUserDataAndFetchWishlist() {

        val userId = sharedPreferences.getInt("user_id", -1) // Varsayılan olarak -1 döner

        // Kullanıcı bilgilerini UI'ye iletmek gerekiyorsa LiveData kullanılabilir (şimdilik direkt olarak işlevsel amaç için kullanılıyor)
        fetchWishlist(userId)
    }

    fun fetchWishlist(userId: Int) {
        val wishlist = mutableListOf<CoursesSql>()
        val cursor = database.query(
            TABLE_COURSE,
            null,
            "is_like = ? AND user_id = ?",
            arrayOf("1", userId.toString()),
            null,
            null,
            null
        )
        cursor?.use {
            while (it.moveToNext()) {
                wishlist.add(
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
        _wishlist.postValue(wishlist)
    }
}


