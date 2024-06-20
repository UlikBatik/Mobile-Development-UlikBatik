package com.example.ulikbatik.ui.detailPost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.ulikbatik.data.local.UserPreferences
import com.example.ulikbatik.data.model.LikesModel
import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.data.model.UserModel
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.data.remote.response.LikesResponse
import com.example.ulikbatik.data.remote.response.ResultResponse
import com.example.ulikbatik.data.remote.response.ScrapperResponse
import com.example.ulikbatik.data.repository.PostRepository
import com.example.ulikbatik.data.repository.ScrapRepository

class DetailPostViewModel(
    private val postRepository: PostRepository,
    preferences: UserPreferences,
    userModel: UserModel?,
    private val scrapRepository: ScrapRepository
) : ViewModel() {

    var pref = preferences
    var user = userModel
    val isLoading = postRepository.isLoading
    val isLoadingProduct = scrapRepository.isLoading

    private val _isLiked = MutableLiveData<Boolean>()
    val isLiked: LiveData<Boolean> get() = _isLiked

    fun getPost(postId: String): LiveData<GeneralResponse<PostModel>> {
        return postRepository.getPost(postId)
    }

    fun likePost(userId: String, postId: String): LiveData<LikesResponse> {
        return postRepository.likePost(userId, postId)
    }

    fun getLikes(userId: String, postId: String): LiveData<GeneralResponse<List<LikesModel>>> {
        return postRepository.getLikes(userId).map { likesResponse ->
            val isLiked = likesResponse.data?.any { like -> like.postId == postId } == true
            _isLiked.postValue(isLiked)
            likesResponse
        }
    }

    fun toggleLikeStatus() {
        _isLiked.value = _isLiked.value != true
    }

    fun deletePost(idPost: String): LiveData<GeneralResponse<PostModel>> {
        return postRepository.deletePost(idPost)
    }

    fun getScrapData(batikName: String): LiveData<ResultResponse<List<ScrapperResponse>>> {
        return scrapRepository.getScrapperData(batikName)
    }
}
