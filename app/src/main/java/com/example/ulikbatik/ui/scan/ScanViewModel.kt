package com.example.ulikbatik.ui.scan

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ulikbatik.data.remote.response.ScanResponse
import com.example.ulikbatik.data.repository.ScanRepository
import com.example.ulikbatik.utils.helper.CameraHelper
import com.example.ulikbatik.utils.helper.CameraHelper.reduceFileImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class ScanViewModel(
    private val scanRepository: ScanRepository
) : ViewModel() {

    val isLoading = scanRepository.isLoading

    fun scanImage(attach: Uri, context: Context): LiveData<ScanResponse> {
        val fileImage = CameraHelper.uriToFile(attach, context).reduceFileImage()
        val imageBodyPart = fileImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part =
            MultipartBody.Part.createFormData("attachment", fileImage.name, imageBodyPart)

        return scanRepository.scanImage(imageMultipart)
    }

}