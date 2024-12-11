package com.hasanbasyildiz.learnconnect.downloadedCourse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.hasanbasyildiz.learnconnect.R
import com.hasanbasyildiz.learnconnect.VideoPlayer.VideoPlayerActivity
import com.hasanbasyildiz.learnconnect.databinding.ActivityDownloadedCourseBinding

class DownloadedCourseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDownloadedCourseBinding
    private val viewModel: DownloadedCoursesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadedCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = DownloadedCourseAdapter { course ->
            // Kurs seçildiğinde yapılacak işlemler
            val intent = Intent(this, VideoPlayerActivity::class.java).apply {
                putExtra("videoId", course.videoId)
                putExtra("videoUrl", course.videoPath)
                putExtra("imageUrl", course.imagePath)
                putExtra("courseTitle", course.courseTitle)
            }
            startActivity(intent)
        }
        binding.recyclerView.adapter = adapter

        // Veri gözlemleme
        viewModel.downloadedCourses.observe(this) { courses ->
            adapter.submitList(courses)
        }

        // Verileri yükle
        viewModel.loadDownloadedCourses()
    }
}
