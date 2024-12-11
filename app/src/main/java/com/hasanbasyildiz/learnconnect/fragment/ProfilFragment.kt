package com.hasanbasyildiz.learnconnect.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.hasanbasyildiz.learnconnect.MainActivity
import com.hasanbasyildiz.learnconnect.R
import com.hasanbasyildiz.learnconnect.auth.LoginActivity
import com.hasanbasyildiz.learnconnect.databinding.FragmentMyCoursesBinding
import com.hasanbasyildiz.learnconnect.databinding.FragmentProfilBinding



class ProfilFragment : Fragment() {
    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfilBinding.inflate(inflater, container, false)



        loadUserData()
        setupDarkModeSwitch()
        binding.logoutBtn.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }




        return binding.root
    }

    private fun setupDarkModeSwitch() {
        // SharedPreferences ile dark mode durumunu kontrol et
        val sharedPreferences = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)

        // Switch durumunu ayarla
        binding.darkModeSwitch.isChecked = isDarkMode

        // Switch'in durum değişikliklerini dinle
        binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("dark_mode", isChecked)
            editor.apply()

            // Tema değişikliği
            val nightMode = if (isChecked) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }


    private fun loadUserData() {
        // SharedPreferences üzerinden kullanıcı bilgilerini al
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        val name = sharedPreferences.getString("name", "Ad ")
        val surname = sharedPreferences.getString("surname", "Soyad")
        val email = sharedPreferences.getString("email", "Email")
        val phone = sharedPreferences.getString("phone", "Telefon Numarası")
      //  val user_id = sharedPreferences.getInt("user_id", 12121)

        // Alınan bilgileri arayüzde göster
        binding.profileNameTxt.text = "$name $surname"
        binding.profileMailTxt.text = email
        binding.profilePhoneTxt.text = phone
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}