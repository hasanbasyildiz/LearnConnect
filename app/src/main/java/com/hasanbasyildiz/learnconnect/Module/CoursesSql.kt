package com.hasanbasyildiz.learnconnect.Module

data class CoursesSql(
    val userId: Int,
    val videoId: Int,
    val imageUrl: String,
    val videoUrl: String,
    val courseTitle: String,
    val isLike: Int,
    val isSub: Int
)