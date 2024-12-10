package com.hasanbasyildiz.learnconnect.VideoPlayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.hasanbasyildiz.learnconnect.databinding.ActivityVideoPlayerBinding


class VideoPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoPlayerBinding
    private val viewModel: VideoPlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup ViewModel observers
        setupObservers()

        val userId = intent.getIntExtra("userId", -1)
        val videoId = intent.getIntExtra("videoId", -1)
        val imageUrl = intent.getStringExtra("imageUrl") ?: ""
        val videoUrl = intent.getStringExtra("videoUrl") ?: ""
        val courseTitle = intent.getStringExtra("courseTitle") ?: ""

        // Pass initial data to ViewModel
        viewModel.initialize(userId, videoId, videoUrl, imageUrl, courseTitle)

        binding.buttonindirme.setOnClickListener {
            viewModel.onDownloadButtonClicked()
        }
    }

    private fun setupObservers() {
        viewModel.playbackPosition.observe(this) { position ->
            if (position != null) {
                binding.playerView.player?.seekTo(position)
            }
        }

        viewModel.isVideoDownloaded.observe(this) { isDownloaded ->
            binding.buttonindirme.text = if (isDownloaded) "Videoyu Sil" else "Videoyu İndir"
        }

        viewModel.downloadProgress.observe(this) { progress ->
            if (progress >= 0) {
                // Show progress notification or UI update if necessary
            }
        }

        viewModel.showToastMessage.observe(this) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.notificationVisibility.observe(this) { isVisible ->
            if (!isVisible) {
                // Hide notification
            }
        }

        viewModel.videoPlaybackReady.observe(this) { isReady ->
            if (isReady) {
                binding.playerView.player = viewModel.getPlayerInstance()
                viewModel.startVideoPlayback()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.startVideoPlayback()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseVideoPlayback()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }
}