package com.hasanbasyildiz.learnconnect.CourseDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class CourseDetailViewModel(private val repository: CourseRepository) : ViewModel() {


    private val _courseImageUrl = MutableLiveData<String>()
    val courseImageUrl: LiveData<String> get() = _courseImageUrl
    private val _courseTitle = MutableLiveData<String>()
    val courseTitle: LiveData<String> get() = _courseTitle

    private val _insertResult = MutableLiveData<Boolean>()
    val insertResult: LiveData<Boolean> get() = _insertResult

    private val _updateResult = MutableLiveData<Boolean>()
    val updateResult: LiveData<Boolean> get() = _updateResult

    private val _isAlreadyInserted = MutableLiveData<Boolean>()
    val isAlreadyInserted: LiveData<Boolean> get() = _isAlreadyInserted

    fun checkIfCourseAlreadyInserted(userId: Int, videoId: Int) {
        viewModelScope.launch {
            val isInserted = repository.isCourseInserted(userId, videoId)
            _isAlreadyInserted.postValue(isInserted)
        }
    }

    fun insertOrUpdateCourse(
        userId: Int,
        videoId: Int,
        imageUrl: String,
        videoUrl: String,
        courseTitle: String,
        isLike: Int? = null,
        isSub: Int? = null
    ) {
        viewModelScope.launch {
            val result = repository.insertOrUpdateCourse(userId, videoId, imageUrl, videoUrl, courseTitle, isLike, isSub)
            _insertResult.postValue(result)
        }
    }

    fun updateIsLike(userId: Int, videoId: Int, isLike: Int) {
        viewModelScope.launch {
            val result = repository.updateIsLike(userId, videoId, isLike)
            _updateResult.postValue(result)
        }
    }
    fun setCourseDetails(imageUrl: String, title: String) {
        _courseImageUrl.value = imageUrl
        _courseTitle.value = title

    }
}