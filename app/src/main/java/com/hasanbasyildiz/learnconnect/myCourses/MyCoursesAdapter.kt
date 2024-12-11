package com.hasanbasyildiz.learnconnect.myCourses

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hasanbasyildiz.learnconnect.Module.CoursesSql
import com.hasanbasyildiz.learnconnect.VideoPlayer.VideoPlayerActivity
import com.hasanbasyildiz.learnconnect.databinding.ItemMyCourseBinding
import com.hasanbasyildiz.learnconnect.denemeActivity2


class MyCoursesAdapter : RecyclerView.Adapter<MyCoursesAdapter.CourseViewHolder>() {

    private var courses: List<CoursesSql> = emptyList()

    inner class CourseViewHolder(private val binding: ItemMyCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(course: CoursesSql) {
            binding.courseTitle.text = course.courseTitle

            Glide.with(binding.root.context)
                .load(course.imageUrl)
                .into(binding.courseImage)


            binding.root.setOnClickListener {

                Log.e("CourseClicked", "Course Title: ${course.courseTitle}, Image URL: ${course.imageUrl}")


                val intent = Intent(binding.root.context, VideoPlayerActivity::class.java)

                intent.putExtra("userId", course.userId)
                intent.putExtra("videoId", course.videoId)
                intent.putExtra("imageUrl", course.imageUrl)
                intent.putExtra("videoUrl", course.videoUrl)
                intent.putExtra("courseTitle", course.courseTitle)
                intent.putExtra("isLike", course.isLike)
                intent.putExtra("isSub", course.isSub)
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMyCourseBinding.inflate(inflater, parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(courses[position])
    }

    override fun getItemCount(): Int = courses.size

    fun updateCourses(newCourses: List<CoursesSql>) {
        courses = newCourses
        notifyDataSetChanged()
    }
}
