package com.hasanbasyildiz.learnconnect.myCourses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hasanbasyildiz.learnconnect.databinding.FragmentMyCoursesBinding


class MyCoursesFragment : Fragment() {
    private lateinit var binding: FragmentMyCoursesBinding
    private lateinit var viewModel: MyCoursesViewModel
    private lateinit var adapter: MyCoursesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[MyCoursesViewModel::class.java]
        adapter = MyCoursesAdapter()

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.courses.observe(viewLifecycleOwner) { courses ->
            if (courses.isEmpty()) {
                binding.emptyTextView.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.emptyTextView.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.updateCourses(courses)
            }
        }

        viewModel.fetchCourses(requireContext())
    }
}