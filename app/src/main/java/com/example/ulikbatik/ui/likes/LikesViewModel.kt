package com.example.ulikbatik.ui.likes

import androidx.lifecycle.LiveData
import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.data.repository.LikesRepository
import androidx.lifecycle.ViewModel
import com.example.ulikbatik.data.model.LikesModel

class LikesViewModel(
    private val likesRepository: LikesRepository
) : ViewModel() {

    val isLoading = likesRepository.isLoading

    fun getLikes(userid: String): LiveData<GeneralResponse<List<LikesModel>>> {
        return likesRepository.getLikes(userid)
    }
}