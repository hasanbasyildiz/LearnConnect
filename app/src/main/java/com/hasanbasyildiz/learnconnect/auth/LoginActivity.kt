package com.hasanbasyildiz.learnconnect.auth

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.hasanbasyildiz.learnconnect.MainActivity
import com.hasanbasyildiz.learnconnect.data.LoginHelper
import com.hasanbasyildiz.learnconnect.databinding.ActivityLoginBinding
import com.hasanbasyildiz.learnconnect.denemeActivity2
import com.hasanbasyildiz.learnconnect.downloadedCourse.DownloadedCourseActivity

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var loginHelper: LoginHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginHelper = LoginHelper(this)

        // İnternet bağlantısını kontrol et
        if (isInternetAvailable()) {
            showNoInternetSnackbar()
        }

        binding.loginNewaccountTxt.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginLoginBtn.setOnClickListener {
            val email = binding.loginMail.text.toString()
            val password = binding.loginPasswordEt.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        binding.loginProgressBar.isVisible = true

        val isValidUser = loginHelper.loginUser(email, password)

        binding.loginProgressBar.isVisible = false

        if (isValidUser) {
            val user = loginHelper.getUserData(email)

            if (user != null) {
                val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("user_id", user.id)
                editor.putString("name", user.name)
                editor.putString("surname", user.surname)
                editor.putString("email", user.email)
                editor.putString("phone", user.phone)
                editor.apply()
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Geçersiz e-posta veya şifre", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

    private fun showNoInternetSnackbar() {
        val snackbar = Snackbar.make(binding.root, "İnternet Bağlantısı Yok İndirilen Videolar için Tıklayınız", Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("Git") {
            // Kullanıcıyı başka bir activity'e yönlendirin
            val intent = Intent(this, DownloadedCourseActivity::class.java)
            startActivity(intent)
        }
        snackbar.show()
    }
}
