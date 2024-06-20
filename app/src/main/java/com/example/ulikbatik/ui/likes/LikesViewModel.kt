package com.example.ulikbatik.ui.likes

import androidx.lifecycle.LiveData
import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.data.repository.LikesRepository
import androidx.lifecycle.ViewModel
import com.example.ulikbatik.data.local.UserPreferences
import com.example.ulikbatik.data.model.LikesModel
import com.example.ulikbatik.data.model.UserModel

class LikesViewModel(
    private val likesRepository: LikesRepository,
    userModel: UserModel?
) : ViewModel() {

    var user = userModel
    val isLoading = likesRepository.isLoading

    fun getLikes(userid: String): LiveData<GeneralResponse<List<LikesModel>>> {
        return likesRepository.getLikes(userid)
    }
}