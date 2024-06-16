package com.example.ulikbatik.ui.scan

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ulikbatik.data.model.BatikModel
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.data.remote.response.ResultResponse
import com.example.ulikbatik.data.repository.CatalogRepository
import com.example.ulikbatik.data.repository.ScanRepository
import com.example.ulikbatik.utils.helper.CameraHelper
import com.example.ulikbatik.utils.helper.CameraHelper.reduceFileImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class ScanViewModel(
    private val scanRepository: ScanRepository,
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    val isLoading = catalogRepository.isLoading

    fun scanImage(attach: Uri, context: Context): LiveData<ResultResponse<BatikModel>> {
        val fileImage = CameraHelper.uriToFile(attach, context).reduceFileImage()
        val imageBodyPart = fileImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part =
            MultipartBody.Part.createFormData("attachment", fileImage.name, imageBodyPart)

        return scanRepository.scanImage(imageMultipart)
    }

    fun getBatikTag(batikId:String): LiveData<GeneralResponse<BatikModel>> {
        return catalogRepository.detailCatalog(batikId)
    }

}