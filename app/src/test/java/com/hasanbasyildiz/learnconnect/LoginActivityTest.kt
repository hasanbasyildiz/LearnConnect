package com.hasanbasyildiz.learnconnect

import android.content.SharedPreferences
import com.hasanbasyildiz.learnconnect.Module.User
import com.hasanbasyildiz.learnconnect.auth.LoginActivity
import com.hasanbasyildiz.learnconnect.data.LoginHelper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito

import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], manifest = Config.NONE) // Manifest kontrolünü kapatın
class LoginActivityTest {

    @Mock
    private lateinit var mockLoginHelper: LoginHelper

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    private lateinit var loginActivity: LoginActivity

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        // Mock LoginActivity and dependencies
        loginActivity = Mockito.spy(LoginActivity())
        loginActivity.loginHelper = mockLoginHelper

        Mockito.`when`(mockSharedPreferences.edit()).thenReturn(mockEditor)
        Mockito.`when`(mockEditor.putInt(Mockito.anyString(), Mockito.anyInt())).thenReturn(mockEditor)
        Mockito.`when`(mockEditor.putString(Mockito.anyString(), Mockito.anyString())).thenReturn(mockEditor)
        Mockito.doNothing().`when`(mockEditor).apply()
    }

    @Test
    fun testLoginUser_validCredentials() {
        val email = "test@example.com"
        val password = "password123"
        val user = User(
            id = 1,
            name = "John",
            surname = "Doe",
            email = email,
            phone = "1234567890"
        )

        Mockito.`when`(mockLoginHelper.loginUser(email, password)).thenReturn(true)
        Mockito.`when`(mockLoginHelper.getUserData(email)).thenReturn(user)

        loginActivity.loginUser(email, password)

        Mockito.verify(mockLoginHelper).loginUser(email, password)
        Mockito.verify(mockLoginHelper).getUserData(email)
        Mockito.verify(mockEditor).putInt("user_id", user.id)
        Mockito.verify(mockEditor).putString("name", user.name)
        Mockito.verify(mockEditor).putString("surname", user.surname)
        Mockito.verify(mockEditor).putString("email", user.email)
        Mockito.verify(mockEditor).putString("phone", user.phone)
        Mockito.verify(mockEditor).apply()
    }

    @Test
    fun testLoginUser_invalidCredentials() {
        val email = "test@example.com"
        val password = "wrongpassword"

        Mockito.`when`(mockLoginHelper.loginUser(email, password)).thenReturn(false)

        loginActivity.loginUser(email, password)

        Mockito.verify(mockLoginHelper).loginUser(email, password)
        Mockito.verify(mockLoginHelper, Mockito.never()).getUserData(anyString())
        Mockito.verify(mockEditor, Mockito.never()).putInt(Mockito.anyString(), Mockito.anyInt())
        Mockito.verify(mockEditor, Mockito.never()).putString(Mockito.anyString(), Mockito.anyString())
    }
}

