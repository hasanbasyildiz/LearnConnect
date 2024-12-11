package com.hasanbasyildiz.learnconnect.VideoPlayer

import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.hasanbasyildiz.learnconnect.databinding.ActivityVideoPlayerBinding
import java.io.File


class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var viewModel: VideoPlayerViewModel
    private var playbackPosition: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View binding
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(VideoPlayerViewModel::class.java)

        // Initialize ExoPlayer
        exoPlayer = ExoPlayer.Builder(this).build()
        binding.playerView.player = exoPlayer

        // Fetch intent data
        val userId = intent.getIntExtra("userId", -1)
        val videoId = intent.getIntExtra("videoId", -1)
        val imageUrl = intent.getStringExtra("imageUrl") ?: ""
        val videoUrl = intent.getStringExtra("videoUrl") ?: ""
        val courseTitle = intent.getStringExtra("courseTitle") ?: ""

        viewModel.initializeData(userId, videoId, videoUrl, imageUrl, courseTitle)

        // Observe LiveData for UI updates
        setupObservers()

        // Handle button click
        binding.buttonindirme.setOnClickListener {
            viewModel.handleDownloadButton(videoId, videoUrl, imageUrl, courseTitle)
        }

        // Play video
        viewModel.isVideoDownloaded(videoId, courseTitle).let { isDownloaded ->
            viewModel.setDownloadButtonText(isDownloaded)
            playVideo(videoUrl, videoId, courseTitle)
        }
    }

    private fun setupObservers() {
        viewModel.downloadButtonText.observe(this) { text ->
            binding.buttonindirme.text = text
        }

        viewModel.showToastMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        viewModel.downloadProgress.observe(this) { progress ->
            updateDownloadNotification(progress)
        }
    }

    private fun playVideo(videoUrl: String, videoId: Int, courseTitle: String) {
        val localFile = File(viewModel.getSharedFilePath(videoId, "$courseTitle.mp4"))

        val mediaItem = if (localFile.exists()) {
            MediaItem.fromUri(Uri.fromFile(localFile))
        } else {
            MediaItem.fromUri(videoUrl)
        }

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    private fun updateDownloadNotification(progress: Int) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(this, viewModel.channelId)
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentTitle(if (progress < 100) "İndirme Başladı" else "İndirme Tamamlandı")
            .setContentText(if (progress < 100) "Video indirme işlemi devam ediyor..." else "Video başarıyla indirildi.")
            .setOngoing(progress < 100)
            .setProgress(100, progress, false)

        notificationManager.notify(viewModel.notificationId, builder.build())
    }

    override fun onStart() {
        super.onStart()
        exoPlayer.seekTo(playbackPosition)
        exoPlayer.play()
    }

    override fun onPause() {
        super.onPause()
        playbackPosition = exoPlayer.currentPosition
        exoPlayer.pause()
    }

    override fun onStop() {
        super.onStop()
        playbackPosition = exoPlayer.currentPosition
        exoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }
}
