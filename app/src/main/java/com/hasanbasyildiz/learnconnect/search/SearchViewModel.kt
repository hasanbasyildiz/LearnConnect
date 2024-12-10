package com.hasanbasyildiz.learnconnect.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.hasanbasyildiz.learnconnect.App
import com.hasanbasyildiz.learnconnect.Module.VideoItem
import com.hasanbasyildiz.learnconnect.Module.VideoResponse

class SearchViewModel : ViewModel() {

    private val _videos = MutableLiveData<List<VideoItem>>()
    val videos: LiveData<List<VideoItem>> get() = _videos

    private val _filteredVideos = MutableLiveData<List<VideoItem>>()
    val filteredVideos: LiveData<List<VideoItem>> get() = _filteredVideos

    init {
        loadVideos()
    }

    private fun loadVideos() {
        val json = loadJSONFromAsset()
        _videos.value = json.hits
        _filteredVideos.value = json.hits
    }

    fun filterVideos(query: String) {
        val allVideos = _videos.value ?: emptyList()
        val filtered = allVideos.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.tags.contains(query, ignoreCase = true)
        }
        _filteredVideos.value = filtered
    }

    private fun loadJSONFromAsset(): VideoResponse {
        val json = App.context.assets.open("all.json").bufferedReader().use { it.readText() }
        return Gson().fromJson(json, VideoResponse::class.java)
    }
}