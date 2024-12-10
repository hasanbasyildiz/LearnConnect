package com.hasanbasyildiz.learnconnect.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hasanbasyildiz.learnconnect.CourseDetail.CourseDetailActivity
import com.hasanbasyildiz.learnconnect.Module.VideoItem
import com.hasanbasyildiz.learnconnect.R
import com.hasanbasyildiz.learnconnect.adapter.VideoAdapter
import com.hasanbasyildiz.learnconnect.databinding.ItemVideoBinding
import com.hasanbasyildiz.learnconnect.databinding.ItemVideoSearchBinding


class VideoAdapterSearch(private var videoList: List<VideoItem>) :
    RecyclerView.Adapter<VideoAdapterSearch.VideoViewHolder>() {

    inner class VideoViewHolder(val binding: ItemVideoSearchBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVideoSearchBinding.inflate(inflater, parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]
        holder.binding.apply {
            titleTextView.text = video.title
            Glide.with(thumbnailImageView.context)
                .load(video.videos.medium.thumbnail)
                .into(thumbnailImageView)

            root.setOnClickListener {
                val context = it.context
                val intent = Intent(context, CourseDetailActivity::class.java)
                intent.putExtra("course_id", video.id)

                intent.putExtra("course_image_url", video.videos.medium.thumbnail)
                intent.putExtra("course_title", video.title)
                intent.putExtra("course_price", "$99") // Örnek fiyat
                intent.putExtra("course_rating", "4.5") // Örnek değerlendirme
                intent.putExtra("course_url",video.videos.medium.url)

                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = videoList.size

    fun updateVideos(newVideos: List<VideoItem>) {
        videoList = newVideos
        notifyDataSetChanged()
    }
}