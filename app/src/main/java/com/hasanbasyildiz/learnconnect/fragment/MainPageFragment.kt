package com.hasanbasyildiz.learnconnect.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

import com.hasanbasyildiz.learnconnect.Module.VideoResponse
import com.hasanbasyildiz.learnconnect.R
import com.hasanbasyildiz.learnconnect.adapter.VideoAdapter
import com.hasanbasyildiz.learnconnect.databinding.FragmentMainPageBinding


class MainPageFragment : Fragment() {

    private lateinit var binding: FragmentMainPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using DataBinding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_page, container, false)

        // Load JSON from assets
        val videoResponse = loadJSONFromAsset()

        // Setup RecyclerView
        val adapter = VideoAdapter(videoResponse.hits)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter

        return binding.root
    }

    private fun loadJSONFromAsset(): VideoResponse {
        // JSON'u assets klasöründen oku
        val json: String = requireContext().assets.open("computer.json").bufferedReader().use {
            it.readText()
        }

        // Ham JSON içeriğini yazdır
        println("JSON Content: $json")

        // JSON'u modele dönüştür
        val videoResponse: VideoResponse = Gson().fromJson(json, VideoResponse::class.java)

        // Çözümlenen modelin tüm verilerini yazdır
        println("Parsed Response: $videoResponse")

        // Model elemanlarını detaylıca yazdır
        videoResponse.hits.forEach { video ->
            println("---- Video Details ----")
            println("ID: ${video.id}")
            println("Title: ${video.title}")
            println("Tags: ${video.tags}")
            println("Duration: ${video.duration} seconds")
            println("Video URL: ${video.videos.medium.url}")
            println("Thumbnail: ${video.videos.medium.thumbnail}")
            println("-----------------------")
        }

        return videoResponse
    }




}