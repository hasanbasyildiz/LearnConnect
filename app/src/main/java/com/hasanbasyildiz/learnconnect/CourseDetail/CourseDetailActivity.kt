package com.hasanbasyildiz.learnconnect.CourseDetail

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hasanbasyildiz.learnconnect.databinding.ActivityCourseDetailBinding

class CourseDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCourseDetailBinding
    private lateinit var viewModel: CourseDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = CourseRepository(this)
        val viewModelFactory = CourseDetailViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[CourseDetailViewModel::class.java]

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", 0)

        val courseId = intent.getIntExtra("course_id", 0 )
        val courseImageUrl = intent.getStringExtra("course_image_url") ?: ""
        val courseTitle = intent.getStringExtra("course_title") ?: "No Title"
        val courseUrl = intent.getStringExtra("course_url") ?: "No url"

        viewModel.checkIfCourseAlreadyInserted(courseId)

        viewModel.isAlreadyInserted.observe(this, Observer { isInserted ->
            binding.button1.isEnabled = !isInserted
        })

        binding.button1.setOnClickListener {
            viewModel.insertCourseDetails(
                userId = userId,
                videoId = courseId,
                imageUrl = courseImageUrl,
                videoUrl = courseUrl,
                courseTitle = courseTitle,
                isLike = 0,
                isSub = 1
            )
        }

        viewModel.insertResult.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "Data inserted successfully!", Toast.LENGTH_SHORT).show()
                binding.button1.isEnabled = false
            } else {
                Toast.makeText(this, "Error inserting data!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
