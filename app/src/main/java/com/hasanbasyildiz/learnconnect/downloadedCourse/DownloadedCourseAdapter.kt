package com.hasanbasyildiz.learnconnect.downloadedCourse

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hasanbasyildiz.learnconnect.Module.DownloadedCourse
import com.hasanbasyildiz.learnconnect.databinding.ItemDownloadedCourseBinding

class DownloadedCourseAdapter(
    private val onCourseClicked: (DownloadedCourse) -> Unit
) : ListAdapter<DownloadedCourse, DownloadedCourseAdapter.CourseViewHolder>(
    object : DiffUtil.ItemCallback<DownloadedCourse>() {
        override fun areItemsTheSame(oldItem: DownloadedCourse, newItem: DownloadedCourse): Boolean {
            return oldItem.videoId == newItem.videoId
        }

        override fun areContentsTheSame(oldItem: DownloadedCourse, newItem: DownloadedCourse): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class CourseViewHolder(private val binding: ItemDownloadedCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(course: DownloadedCourse) {
            binding.courseTitle.text = course.courseTitle
            binding.courseImage.setImageURI(Uri.parse(course.imagePath))
            binding.root.setOnClickListener {
                onCourseClicked(course)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemDownloadedCourseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
