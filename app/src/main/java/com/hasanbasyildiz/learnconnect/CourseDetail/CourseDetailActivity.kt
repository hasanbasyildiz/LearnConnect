package com.hasanbasyildiz.learnconnect.CourseDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.hasanbasyildiz.learnconnect.R
import com.hasanbasyildiz.learnconnect.databinding.ActivityCourseDetailBinding

class CourseDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCourseDetailBinding
    private val viewModel: CourseDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent ile gelen verileri ViewModel'e aktar
        val courseImageUrl = intent.getStringExtra("course_image_url") ?: ""
        val courseTitle = intent.getStringExtra("course_title") ?: "No Title"
        val coursePrice = intent.getStringExtra("course_price") ?: "No Price"
        val courseRating = intent.getStringExtra("course_rating") ?: "No Rating"
        val courseId = intent.getStringExtra("cours_id") ?: "No id"
        val courseUrl = intent.getStringExtra("cours_id") ?: "No url"

        viewModel.setCourseDetails(courseImageUrl, courseTitle, coursePrice, courseRating)

        // Verileri gözlemle ve UI'ye aktar
        viewModel.courseImageUrl.observe(this, Observer { imageUrl ->
            Glide.with(this).load(imageUrl).into(binding.detailImage)
        })

        viewModel.courseTitle.observe(this, Observer { title ->
            binding.detailTitle.text = title
        })

        viewModel.coursePrice.observe(this, Observer { price ->
            binding.detailPrice.text = price
        })

        viewModel.courseRating.observe(this, Observer { rating ->
            binding.detailRating.text = rating
        })


        // Buton tıklama olayları
        binding.button1.setOnClickListener {
            Toast.makeText(this, "Button 1 clicked", Toast.LENGTH_SHORT).show()
        }

        binding.button2.setOnClickListener {
            Toast.makeText(this, "Button 2 clicked", Toast.LENGTH_SHORT).show()
        }



    }


}