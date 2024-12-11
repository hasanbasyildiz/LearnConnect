package com.hasanbasyildiz.learnconnect.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.hasanbasyildiz.learnconnect.data.DatabaseHelper.Companion.COLUMN_NAME
import com.hasanbasyildiz.learnconnect.data.DatabaseHelper.Companion.COLUMN_PHONE
import com.hasanbasyildiz.learnconnect.data.DatabaseHelper.Companion.COLUMN_SURNAME
import com.hasanbasyildiz.learnconnect.data.DatabaseHelper.Companion.COLUMN_USER_ID
import com.hasanbasyildiz.learnconnect.Module.User

import java.security.MessageDigest

class LoginHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "learnconnect.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_USERS = "users"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD_HASH = "password_hash"
    }


    override fun onCreate(db: SQLiteDatabase?) {}

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}


    private fun sha256(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(input.toByteArray())
        val stringBuilder = StringBuilder()

        for (byte in hashBytes) {
            stringBuilder.append(String.format("%02x", byte))
        }

        return stringBuilder.toString()
    }


    fun loginUser(email: String, password: String): Boolean {
        val db = readableDatabase


        val hashedPassword = sha256(password)


        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD_HASH = ?",
            arrayOf(email, hashedPassword)
        )

        val userExists = cursor.moveToFirst()
        cursor.close()
        db.close()

        return userExists
    }

    fun getUserData(email: String): User? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?", arrayOf(email))
        var user: User? = null

        if (cursor.moveToFirst()) {
            val userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val surname = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SURNAME))
            val phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
            val emailFromDB = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))



            user = User(userId, name, surname, emailFromDB, phone)
        }
        cursor.close()
        db.close()
        return user
    }
}
