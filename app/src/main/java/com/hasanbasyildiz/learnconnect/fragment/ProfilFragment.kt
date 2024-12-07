package com.hasanbasyildiz.learnconnect.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hasanbasyildiz.learnconnect.R
import com.hasanbasyildiz.learnconnect.databinding.FragmentMyCoursesBinding
import com.hasanbasyildiz.learnconnect.databinding.FragmentProfilBinding



class ProfilFragment : Fragment() {
    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfilBinding.inflate(inflater, container, false)

        //  binding.toolbar.title="Profile"

        loadUserData()


        return binding.root
    }

    private fun loadUserData() {
        // SharedPreferences üzerinden kullanıcı bilgilerini al
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        val name = sharedPreferences.getString("name", "Ad Soyad")
        val email = sharedPreferences.getString("email", "Email")
        val phone = sharedPreferences.getString("phone", "Telefon Numarası")

        // Alınan bilgileri arayüzde göster
        binding.profileNameTxt.text = name
        binding.profileMailTxt.text = email
        binding.profilePhoneTxt.text = phone
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}