package com.hasanbasyildiz.learnconnect.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.hasanbasyildiz.learnconnect.MainActivity
import com.hasanbasyildiz.learnconnect.data.LoginHelper
import com.hasanbasyildiz.learnconnect.databinding.ActivityLoginBinding
import com.hasanbasyildiz.learnconnect.denemeActivity2

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var loginHelper: LoginHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginHelper = LoginHelper(this)



        binding.loginNewaccountTxt.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginLoginBtn.setOnClickListener {
            val email = binding.loginMail.text.toString()
            val password = binding.loginPasswordEt.text.toString()

            // Basic validation
            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.button.setOnClickListener {
            val intent = Intent(this, denemeActivity2::class.java)
            startActivity(intent)
        }


    }

    private fun loginUser(email: String, password: String) {
        // İlerleme çubuğunu göster
        binding.loginProgressBar.isVisible= true


        // Giriş yapmayı dene
        val isValidUser = loginHelper.loginUser(email, password)

        // İlerleme çubuğunu gizle


        if (isValidUser) {
            val user = loginHelper.getUserData(email)


            binding.loginProgressBar.isVisible= false
            // Başarılı giriş, ana ekrana geçiş
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


            val intent = Intent(this, MainActivity::class.java) // Ana ekran
            startActivity(intent)
            finish()

        } else {

            Toast.makeText(this, "Geçersiz e-posta veya şifre", Toast.LENGTH_SHORT).show()
        }
    }

}