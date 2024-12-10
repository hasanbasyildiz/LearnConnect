package com.hasanbasyildiz.learnconnect.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.hasanbasyildiz.learnconnect.Module.VideoResponse
import com.hasanbasyildiz.learnconnect.R
import com.hasanbasyildiz.learnconnect.adapter.VideoAdapter
import com.hasanbasyildiz.learnconnect.databinding.FragmentSearchBinding



class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var adapter: VideoAdapterSearch

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        // ViewModel oluşturma
        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        setupRecyclerView()
        setupSearchFunctionality()
        observeLiveData()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = VideoAdapterSearch(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun setupSearchFunctionality() {
        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchViewModel.filterVideos(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun observeLiveData() {
        searchViewModel.filteredVideos.observe(viewLifecycleOwner) { videos ->
            adapter.updateVideos(videos)
        }
    }
}
