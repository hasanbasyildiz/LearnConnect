package com.hasanbasyildiz.learnconnect.wishlist

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hasanbasyildiz.learnconnect.Module.CoursesSql
import com.hasanbasyildiz.learnconnect.VideoPlayer.VideoPlayerActivity
import com.hasanbasyildiz.learnconnect.databinding.ItemWishlistBinding

class WishlistAdapter : RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {

    private var wishlist: List<CoursesSql> = emptyList()

    inner class WishlistViewHolder(private val binding: ItemWishlistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CoursesSql) {
            binding.itemTitle.text = item.courseTitle
            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .into(binding.itemImage)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWishlistBinding.inflate(inflater, parent, false)
        return WishlistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        holder.bind(wishlist[position])
    }

    override fun getItemCount(): Int = wishlist.size

    fun updateWishlist(newWishlist: List<CoursesSql>) {
        wishlist = newWishlist
        notifyDataSetChanged()
    }
}
