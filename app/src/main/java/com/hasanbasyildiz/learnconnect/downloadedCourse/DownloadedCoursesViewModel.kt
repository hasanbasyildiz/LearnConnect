package com.hasanbasyildiz.learnconnect.downloadedCourse

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hasanbasyildiz.learnconnect.Module.DownloadedCourse
import java.io.File

class DownloadedCoursesViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    private val _downloadedCourses = MutableLiveData<List<DownloadedCourse>>()
    val downloadedCourses: LiveData<List<DownloadedCourse>> get() = _downloadedCourses

    fun loadDownloadedCourses() {
        val sharedDir = File(context.filesDir, "shared")
        if (sharedDir.exists()) {
            val courses = sharedDir.listFiles()?.mapNotNull { file ->
                val fileNameWithoutExtension = file.name.substringBeforeLast(".")
                val parts = fileNameWithoutExtension.split("_")
                if (parts.size == 2) {
                    val videoId = parts[0].toIntOrNull()
                    val courseTitle = parts[1]
                    if (file.extension == "mp4" && videoId != null) {
                        val imageFile = File(sharedDir, "${videoId}_image.jpg")
                        DownloadedCourse(
                            videoId = videoId,
                            courseTitle = courseTitle,
                            videoPath = file.absolutePath,
                            imagePath = if (imageFile.exists()) imageFile.absolutePath else ""
                        )
                    } else null
                } else null
            }.orEmpty()
            _downloadedCourses.value = courses
        } else {
            _downloadedCourses.value = emptyList()
        }
    }
}
