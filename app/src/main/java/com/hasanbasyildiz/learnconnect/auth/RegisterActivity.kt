package com.hasanbasyildiz.learnconnect.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.hasanbasyildiz.learnconnect.data.DatabaseHelper
import com.hasanbasyildiz.learnconnect.databinding.ActivityRegisterBinding
import java.security.MessageDigest

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        binding.registerSigninBtn.setOnClickListener {
            handleSignup()
        }
    }

    private fun handleSignup() {

        val name = binding.registerName.text.toString().trim()
        val surname = binding.registerSurname.text.toString().trim()
        val email = binding.registerMail.text.toString().trim()
        val phone = binding.registerTel.text.toString().trim()
        val password = binding.registerPasswordEt.text.toString().trim()
        val passwordConfirm = binding.registerPasswordEtConfirm.text.toString().trim()


        if (!validateInputs(name, surname, email, password, passwordConfirm)) {
            return
        }


        val passwordHash = hashPassword(password)


        signupDatabase(name, surname, email, phone, passwordHash)
    }

    private fun signupDatabase(name: String, surname: String, email: String, phone: String?, passwordHash: String) {
        val isSuccess = databaseHelper.registerUser(name, surname, email, phone, passwordHash)
        if (isSuccess) {
            Toast.makeText(this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Kayıt başarısız! E-posta zaten kayıtlı olabilir.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(name: String, surname: String, email: String, password: String, passwordConfirm: String): Boolean {
        return when {
            name.isEmpty() -> {
                showToast("İsim alanı boş olamaz!")
                false
            }
            surname.isEmpty() -> {
                showToast("Soyisim alanı boş olamaz!")
                false
            }
            email.isEmpty() -> {
                showToast("E-posta alanı boş olamaz!")
                false
            }
            !isValidEmail(email) -> {
                showToast("Geçerli bir e-posta adresi girin!")
                false
            }
            password.isEmpty() -> {
                showToast("Şifre alanı boş olamaz!")
                false
            }
            password.length < 5 -> {
                showToast("Şifre en az 5 karakter olmalıdır!")
                false
            }
            password != passwordConfirm -> {
                showToast("Şifreler eşleşmiyor!")
                false
            }
            else -> true
        }
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}