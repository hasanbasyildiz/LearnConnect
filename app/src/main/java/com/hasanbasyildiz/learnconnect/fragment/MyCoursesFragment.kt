package com.hasanbasyildiz.learnconnect.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hasanbasyildiz.learnconnect.R
import com.hasanbasyildiz.learnconnect.databinding.FragmentMainPageBinding
import com.hasanbasyildiz.learnconnect.databinding.FragmentMyCoursesBinding


class MyCoursesFragment : Fragment() {
    private lateinit var binding:FragmentMyCoursesBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyCoursesBinding.inflate(inflater,container,false)


        return binding.root
    }


}