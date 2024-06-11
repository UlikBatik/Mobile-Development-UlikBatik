package com.example.ulikbatik.ui.upload

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ulikbatik.data.model.UserModel
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.data.remote.response.PostResponse
import com.example.ulikbatik.data.repository.PostRepository
import com.example.ulikbatik.utils.helper.CameraHelper
import com.example.ulikbatik.utils.helper.CameraHelper.reduceFileImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UploadViewModel(
    private val postRepository: PostRepository,
    userModel: UserModel?
) : ViewModel() {

    val isLoading = postRepository.isLoading
    val user = userModel


    fun uploadPost(
        image: Uri, caption: String,
        userId: String,
        batikId: String,
        context: Context
    ): LiveData<GeneralResponse<PostResponse>> {
        val fileImage = CameraHelper.uriToFile(image, context).reduceFileImage()
        val imageBodyPart = fileImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part =
            MultipartBody.Part.createFormData("IMAGE", fileImage.name, imageBodyPart)
        val captionPart = caption.toRequestBody("text/plain".toMediaTypeOrNull())
        val userIdPart = userId.toRequestBody("text/plain".toMediaTypeOrNull())
        val batikIdPart = batikId.toRequestBody("text/plain".toMediaTypeOrNull())

        return postRepository.createPost(imageMultipart, captionPart, userIdPart, batikIdPart)
    }

}