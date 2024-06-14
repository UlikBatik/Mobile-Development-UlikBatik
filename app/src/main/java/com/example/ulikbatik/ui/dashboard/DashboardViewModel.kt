package com.example.ulikbatik.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ulikbatik.data.local.UserPreferences
import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.data.model.UserModel
import com.example.ulikbatik.data.repository.PostRepository

class DashboardViewModel(
    private val postRepository: PostRepository,
    preferences: UserPreferences,
    userModel: UserModel?
) : ViewModel() {

    var pref = preferences
    var user = userModel

    val isLoading = postRepository.isLoading

    fun getPosts(): LiveData<PagingData<PostModel>> {
        return postRepository.getAllPost().cachedIn(viewModelScope)
    }

}