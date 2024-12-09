package com.hasanbasyildiz.learnconnect.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "learnconnect.db"
        private const val DATABASE_VERSION = 1

        // Course tablosu
        const val TABLE_COURSE = "course"
        const val COLUMN_USER_ID = "user_id" // FOREIGN KEY
        const val COLUMN_VIDEO_ID = "video_id"
        const val COLUMN_IMAGE_URL = "image_url"
        const val COLUMN_VIDEO_URL = "video_url"
        const val COLUMN_COURSE_TITLE = "course_title"
        const val COLUMN_IS_LIKE = "is_like"
        const val COLUMN_IS_SUB = "is_sub"

        // Users tablosu
        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID_PK = "user_id" // PRIMARY KEY
        const val COLUMN_NAME = "name"
        const val COLUMN_SURNAME = "surname"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_PASSWORD_HASH = "password_hash"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Users tablosu oluşturma
        val createUsersTableQuery = """
            CREATE TABLE IF NOT EXISTS $TABLE_USERS (
                $COLUMN_USER_ID_PK INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_SURNAME TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL UNIQUE,
                $COLUMN_PHONE TEXT,
                $COLUMN_PASSWORD_HASH TEXT NOT NULL
            )
        """.trimIndent()

        // Course tablosu oluşturma (Yabancı Anahtar ile)
        val createCourseTableQuery = """
            CREATE TABLE IF NOT EXISTS $TABLE_COURSE (
                $COLUMN_USER_ID INTEGER,
                $COLUMN_VIDEO_ID INTEGER,
                $COLUMN_IMAGE_URL TEXT,
                $COLUMN_VIDEO_URL TEXT,
                $COLUMN_COURSE_TITLE TEXT,
                $COLUMN_IS_LIKE INTEGER DEFAULT NULL,
                $COLUMN_IS_SUB INTEGER DEFAULT NULL,
                FOREIGN KEY ($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID_PK)
                ON DELETE CASCADE
                ON UPDATE CASCADE
            )
        """.trimIndent()

        db?.execSQL(createUsersTableQuery)
        db?.execSQL(createCourseTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Tabloları güncelle
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_COURSE")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // Kullanıcı kaydı ekleme fonksiyonu
    fun registerUser(name: String, surname: String, email: String, phone: String?, passwordHash: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_SURNAME, surname)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PHONE, phone)
            put(COLUMN_PASSWORD_HASH, passwordHash)
        }

        val result = db.insert(TABLE_USERS, null, values)
        db.close()

        return result != -1L
    }
}
