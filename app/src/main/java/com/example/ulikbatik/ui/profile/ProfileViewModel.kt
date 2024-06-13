package com.example.ulikbatik.ui.profile


import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ulikbatik.data.local.UserPreferences
import com.example.ulikbatik.data.model.UserModel
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.data.remote.response.ProfileUserResponse
import com.example.ulikbatik.data.repository.UserRepository
import com.example.ulikbatik.utils.helper.CameraHelper
import com.example.ulikbatik.utils.helper.CameraHelper.reduceFileImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProfileViewModel(
    private val userRepository: UserRepository,
    userModel: UserModel?,
    preferences: UserPreferences
) : ViewModel() {

    val user = userModel
    val pref = preferences
    val isLoading = userRepository.isLoading

    fun getUserData(id: String): LiveData<ProfileUserResponse> {
        return userRepository.getProfileUser(id)
    }

    fun saveProfileUser(
        image: Uri?,
        newUsername: String,
        userId: String,
        context: Context
    ): LiveData<GeneralResponse<UserModel>> {
        if (image != null) {
            val fileImage = CameraHelper.uriToFile(image, context).reduceFileImage()
            val imageBodyPart = fileImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part =
                MultipartBody.Part.createFormData("IMAGE", fileImage.name, imageBodyPart)
            val usernamePart = newUsername.toRequestBody("text/plain".toMediaTypeOrNull())
            return userRepository.saveIncludeImage(imageMultipart, usernamePart, userId)
        }
        val usernamePart = newUsername.toRequestBody("text/plain".toMediaTypeOrNull())
        return userRepository.saveOnlyUsername( usernamePart, userId)
    }
}