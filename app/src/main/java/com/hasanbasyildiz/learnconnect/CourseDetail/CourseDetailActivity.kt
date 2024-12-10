package com.hasanbasyildiz.learnconnect.CourseDetail

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
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

        val courseId = intent.getIntExtra("course_id", 0)
        val courseImageUrl = intent.getStringExtra("course_image_url") ?: ""
        val courseTitle = intent.getStringExtra("course_title") ?: "No Title"
        val courseUrl = intent.getStringExtra("course_url") ?: "No url"

        viewModel.setCourseDetails(courseImageUrl, courseTitle)

        viewModel.checkIfCourseAlreadyInserted(userId, courseId)

        viewModel.courseImageUrl.observe(this, Observer { imageUrl ->
            Glide.with(this).load(imageUrl).into(binding.detailImage)
        })
        viewModel.courseTitle.observe(this, Observer { title ->
            binding.detailTitle.text = title
        })

        viewModel.isAlreadyInserted.observe(this, Observer { isInserted ->
            binding.button1.isEnabled = !isInserted
        })

        binding.button1.setOnClickListener {
            viewModel.insertOrUpdateCourse(
                userId = userId,
                videoId = courseId,
                imageUrl = courseImageUrl,
                videoUrl = courseUrl,
                courseTitle = courseTitle,
                isLike = 1 // isLike'ı 1 olarak günceller
            )
        }

// Button 2 için isSub'ı güncelle
        binding.button2.setOnClickListener {
            viewModel.insertOrUpdateCourse(
                userId = userId,
                videoId = courseId,
                imageUrl = courseImageUrl,
                videoUrl = courseUrl,
                courseTitle = courseTitle,
                isSub = 1 // isSub'ı 1 olarak günceller
            )
        }

        viewModel.insertResult.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "Data updated/inserted successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error in operation!", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.updateResult.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "isLike updated successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error updating isLike!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}