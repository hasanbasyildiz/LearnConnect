package com.hasanbasyildiz.learnconnect.CourseDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class CourseDetailViewModel(private val repository: CourseRepository) : ViewModel() {

    private val _insertResult = MutableLiveData<Boolean>()
    val insertResult: LiveData<Boolean> get() = _insertResult

    private val _isAlreadyInserted = MutableLiveData<Boolean>()
    val isAlreadyInserted: LiveData<Boolean> get() = _isAlreadyInserted

    fun checkIfCourseAlreadyInserted(videoId: Int) {
        viewModelScope.launch {
            val isInserted = repository.isCourseInserted(videoId)
            _isAlreadyInserted.postValue(isInserted)
        }
    }

    fun insertCourseDetails(userId: Int, videoId: Int, imageUrl: String, videoUrl: String, courseTitle: String, isLike: Int, isSub: Int) {
        viewModelScope.launch {
            val result = repository.insertCourse(userId, videoId, imageUrl, videoUrl, courseTitle, isLike, isSub)
            _insertResult.postValue(result)
        }
    }
}