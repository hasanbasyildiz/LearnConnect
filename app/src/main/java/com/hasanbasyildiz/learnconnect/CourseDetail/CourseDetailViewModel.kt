package com.hasanbasyildiz.learnconnect.CourseDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CourseDetailViewModel : ViewModel() {

    private val _courseImageUrl = MutableLiveData<String>()
    val courseImageUrl: LiveData<String> get() = _courseImageUrl

    private val _courseTitle = MutableLiveData<String>()
    val courseTitle: LiveData<String> get() = _courseTitle

    private val _coursePrice = MutableLiveData<String>()
    val coursePrice: LiveData<String> get() = _coursePrice

    private val _courseRating = MutableLiveData<String>()
    val courseRating: LiveData<String> get() = _courseRating



    fun setCourseDetails(imageUrl: String, title: String, price: String, rating: String) {
        _courseImageUrl.value = imageUrl
        _courseTitle.value = title
        _coursePrice.value = price
        _courseRating.value = rating
    }
}