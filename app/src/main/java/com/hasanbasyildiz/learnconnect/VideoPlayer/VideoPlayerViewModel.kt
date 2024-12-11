package com.hasanbasyildiz.learnconnect.VideoPlayer

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class VideoPlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    val downloadButtonText = MutableLiveData<String>()
    val showToastMessage = MutableLiveData<String>()
    val downloadProgress = MutableLiveData<Int>()

    val notificationId = 1
    val channelId = "video_download_channel"

    fun initializeData(
        userId: Int,
        videoId: Int,
        videoUrl: String,
        imageUrl: String,
        courseTitle: String
    ) {
        Log.e("infouserId", userId.toString())
        Log.e("infovideoId", videoId.toString())
        Log.e("infovideoUrl", videoUrl)
        Log.e("infoimageUrl", imageUrl)
        Log.e("infocourseTitle", courseTitle)
    }

    fun handleDownloadButton(
        videoId: Int,
        videoUrl: String,
        imageUrl: String,
        courseTitle: String
    ) {
        if (isVideoDownloaded(videoId, courseTitle)) {
            deleteVideo(videoId, courseTitle)
        } else {
            downloadVideo(videoUrl, videoId, courseTitle)
            downloadImage(imageUrl, videoId)
        }
    }

    fun setDownloadButtonText(isDownloaded: Boolean) {
        downloadButtonText.postValue(if (isDownloaded) "Videoyu Sil" else "Videoyu İndir")
    }

    fun isVideoDownloaded(videoId: Int, courseTitle: String): Boolean {
        val file = File(getSharedFilePath(videoId, "$courseTitle.mp4"))
        return file.exists()
    }

    fun getSharedFilePath(videoId: Int, fileName: String): String {
        val sharedDir = File(context.filesDir, "shared")
        if (!sharedDir.exists()) {
            sharedDir.mkdir()
        }
        return File(sharedDir, "${videoId}_$fileName").absolutePath
    }

    private fun downloadVideo(url: String, videoId: Int, courseTitle: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                showToastMessage.postValue("Video indirme başarısız!")
                downloadButtonText.postValue("Videoyu İndir")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val file = File(getSharedFilePath(videoId, "$courseTitle.mp4"))
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

                        downloadProgress.postValue(progress)
                    }

                    fos.close()
                    inputStream?.close()

                    showToastMessage.postValue("Video başarıyla indirildi!")
                    downloadButtonText.postValue("Videoyu Sil")
                    downloadProgress.postValue(100) // Tamamlandı
                }
            }
        })
    }

    private fun downloadImage(url: String, videoId: Int) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                showToastMessage.postValue("Görsel indirme başarısız!")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val file = File(getSharedFilePath(videoId, "image.jpg"))
                    val fos = FileOutputStream(file)

                    val buffer = ByteArray(4096)
                    val inputStream = response.body?.byteStream()
                    var bytesRead: Int

                    while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
                        fos.write(buffer, 0, bytesRead)
                    }

                    fos.close()
                    inputStream?.close()

                    showToastMessage.postValue("Görsel başarıyla indirildi!")
                }
            }
        })
    }

    private fun deleteVideo(videoId: Int, courseTitle: String) {
        val videoFile = File(getSharedFilePath(videoId, "$courseTitle.mp4"))
        val imageFile = File(getSharedFilePath(videoId, "image.jpg"))

        if (videoFile.exists()) videoFile.delete()
        if (imageFile.exists()) imageFile.delete()

        showToastMessage.postValue("Video ve görsel başarıyla silindi!")
        downloadButtonText.postValue("Videoyu İndir")
    }
}


