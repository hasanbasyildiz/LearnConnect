package com.hasanbasyildiz.learnconnect


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import androidx.core.app.NotificationCompat

import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.hasanbasyildiz.learnconnect.databinding.ActivityDeneme2Binding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class denemeActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityDeneme2Binding
    private lateinit var exoPlayer: ExoPlayer
    private var playbackPosition: Long = 0

    private val videoUrl =
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
    private val fileName = "COSTA_RICA_VIDEO.mp4"
    private val notificationId = 1
    private val channelId = "video_download_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDeneme2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        createNotificationChannel()


        exoPlayer = ExoPlayer.Builder(this).build()
        binding.playerView.player = exoPlayer


        playVideo()

        if (isVideoDownloaded()) {
            binding.buttonindirme.text = "Videoyu Sil"
        } else {
            binding.buttonindirme.text = "Videoyu İndir"
        }


        binding.buttonindirme.setOnClickListener {
            if (isVideoDownloaded()) {
                deleteVideo()
            } else {
                downloadVideo(videoUrl, fileName)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Video Download"
            val descriptionText = "Notifications for video download progress"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun playVideo() {
        val localFile = File(filesDir, fileName)

        if (localFile.exists()) {
            val mediaItem = MediaItem.fromUri(Uri.fromFile(localFile))
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        } else {
            val mediaItem = MediaItem.fromUri(videoUrl)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        }
    }

    private fun downloadVideo(url: String, fileName: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        // İndirme işlemi başlat
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@denemeActivity2, "İndirme başarısız!", Toast.LENGTH_SHORT).show()
                    binding.buttonindirme.isEnabled = true
                    hideNotification()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val file = File(filesDir, fileName)
                    val fos = FileOutputStream(file)

                    val buffer = ByteArray(4096)
                    val inputStream = response.body?.byteStream()
                    var bytesRead: Int
                    var totalBytesRead = 0L
                    val contentLength = response.body?.contentLength() ?: -1L

                    while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
                        fos.write(buffer, 0, bytesRead)
                        totalBytesRead += bytesRead

                        val progress = if (contentLength > 0) {
                            (totalBytesRead * 100 / contentLength).toInt()
                        } else {
                            -1
                        }

                        updateDownloadNotification(progress)
                    }

                    fos.close()
                    inputStream?.close()

                    runOnUiThread {
                        updateDownloadNotification(100)
                        hideNotification()
                        binding.buttonindirme.text = "Videoyu Sil"
                        binding.buttonindirme.isEnabled = true
                        Toast.makeText(this@denemeActivity2, "Video başarıyla indirildi!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        binding.buttonindirme.isEnabled = false
        updateDownloadNotification(0)
    }

    private fun updateDownloadNotification(progress: Int) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("İndirme Başladı")
            .setContentText("Video indirme işlemi devam ediyor")
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setProgress(100, progress, false)
            .setOngoing(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    private fun hideNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId) // Bildirimi gizle
    }

    private fun deleteVideo() {
        val file = File(filesDir, fileName)
        if (file.exists()) {
            file.delete()
            binding.buttonindirme.text = "Videoyu İndir"
            binding.buttonindirme.isEnabled = true
            Toast.makeText(this, "Video başarıyla silindi!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isVideoDownloaded(): Boolean {
        val file = File(filesDir, fileName)
        return file.exists()
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