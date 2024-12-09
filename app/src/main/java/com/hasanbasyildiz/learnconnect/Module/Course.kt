package com.hasanbasyildiz.learnconnect.Module

data class VideoItem(
    val id: Int,
    val pageURL: String,
    val type: String,
    val tags: String,
    val duration: Int,
    val title: String,
    val videos: VideoDetails
)

data class VideoDetails(
    val medium: VideoMedium
)

data class VideoMedium(
    val url: String,
    val width: Int,
    val height: Int,
    val size: Int,
    val thumbnail: String
)

data class VideoResponse(
    val hits: List<VideoItem>
)