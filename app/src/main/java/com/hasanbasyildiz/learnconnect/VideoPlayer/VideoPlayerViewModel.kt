package com.hasanbasyildiz.learnconnect.VideoPlayer

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import java.io.File

class VideoPlayerViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    private val exoPlayer: ExoPlayer by lazy {
        ExoPlayer.Builder(context).build()
    }

    private val _playbackPosition = MutableLiveData<Long>(0)
    val playbackPosition: LiveData<Long> get() = _playbackPosition

    private val _isVideoDownloaded = MutableLiveData<Boolean>(false)
    val isVideoDownloaded: LiveData<Boolean> get() = _isVideoDownloaded

    private val _downloadProgress = MutableLiveData<Int>(-1)
    val downloadProgress: LiveData<Int> get() = _downloadProgress

    private val _showToastMessage = MutableLiveData<String>()
    val showToastMessage: LiveData<String> get() = _showToastMessage

    private val _notificationVisibility = MutableLiveData<Boolean>(false)
    val notificationVisibility: LiveData<Boolean> get() = _notificationVisibility

    private val _videoPlaybackReady = MutableLiveData<Boolean>(false)
    val videoPlaybackReady: LiveData<Boolean> get() = _videoPlaybackReady

    private var videoId: Int = -1
    private lateinit var videoUrl: String
    private lateinit var imageUrl: String

    fun initialize(userId: Int, videoId: Int, videoUrl: String, imageUrl: String, courseTitle: String) {
        this.videoId = videoId
        this.videoUrl = videoUrl
        this.imageUrl = imageUrl
        _isVideoDownloaded.value = isVideoDownloaded(videoId)
        playVideo()
    }

    fun getPlayerInstance(): ExoPlayer = exoPlayer

    fun startVideoPlayback() {
        exoPlayer.seekTo(_playbackPosition.value ?: 0)
        exoPlayer.play()
    }

    fun pauseVideoPlayback() {
        _playbackPosition.value = exoPlayer.currentPosition
        exoPlayer.pause()
    }

    fun releasePlayer() {
        exoPlayer.release()
    }

    private fun playVideo() {
        val mediaItem = if (isVideoDownloaded(videoId)) {
            val localFile = File(getSharedFilePath(videoId, "video.mp4"))
            MediaItem.fromUri(Uri.fromFile(localFile))
        } else {
            MediaItem.fromUri(videoUrl)
        }
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        _videoPlaybackReady.value = true
    }

    fun onDownloadButtonClicked() {
        if (isVideoDownloaded(videoId)) {
            deleteVideo()
        } else {
            downloadVideo()
            downloadImage()
        }
    }

    private fun downloadVideo() {
        // Implement video download logic (similar to the activity, but trigger LiveData updates instead of directly modifying UI)
        _downloadProgress.value = 0
        // Simulate download logic
    }

    private fun downloadImage() {
        // Implement image download logic
    }

    private fun deleteVideo() {
        val videoFile = File(getSharedFilePath(videoId, "video.mp4"))
        val imageFile = File(getSharedFilePath(videoId, "image.jpg"))
        if (videoFile.exists()) videoFile.delete()
        if (imageFile.exists()) imageFile.delete()
        _isVideoDownloaded.value = false
        _showToastMessage.value = "Video ve görsel başarıyla silindi!"
    }

    private fun isVideoDownloaded(videoId: Int): Boolean {
        val file = File(getSharedFilePath(videoId, "video.mp4"))
        return file.exists()
    }

    private fun getSharedFilePath(videoId: Int, fileName: String): String {
        val sharedDir = File(context.filesDir, "shared")
        if (!sharedDir.exists()) {
            sharedDir.mkdir()
        }
        return File(sharedDir, "${videoId}_$fileName").absolutePath
    }
}
