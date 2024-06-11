package com.example.ulikbatik.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ulikbatik.data.local.UserPreferences
import com.example.ulikbatik.data.model.UserModel
import com.example.ulikbatik.data.remote.config.ApiService
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.data.remote.response.ProfileUserResponse
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(
    private val apiService: ApiService,
    private val pref : UserPreferences
) {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    fun getProfileUser(userId: String): LiveData<GeneralResponse<ProfileUserResponse>> {
        _isLoading.value = true
        val responseJson = MutableLiveData<GeneralResponse<ProfileUserResponse>>()
        val client = apiService.getUserProfile(userId)
        client.enqueue(
            object : Callback<GeneralResponse<ProfileUserResponse>> {
                override fun onResponse(
                    call: Call<GeneralResponse<ProfileUserResponse>>,
                    response: Response<GeneralResponse<ProfileUserResponse>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        responseJson.value = response.body()
                    } else {
                        responseJson.value = GeneralResponse(
                            message = response.code().toString(),
                            status = false
                        )
                    }
                }

                override fun onFailure(
                    call: Call<GeneralResponse<ProfileUserResponse>>,
                    t: Throwable
                ) {
                    _isLoading.value = false
                    responseJson.value = GeneralResponse(
                        message = "500",
                        status = false
                    )
                }
            }
        )
        return responseJson
    }


    fun saveImage(
        image: MultipartBody.Part,
        username: RequestBody,
        userId: String
    ): LiveData<GeneralResponse<UserModel>> {
        _isLoading.value = true
        val responseJson = MutableLiveData<GeneralResponse<UserModel>>()
        val client = apiService.updateProfile(userId, image, username)
        client.enqueue(
            object : Callback<GeneralResponse<UserModel>> {
                override fun onResponse(
                    call: Call<GeneralResponse<UserModel>>,
                    response: Response<GeneralResponse<UserModel>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        responseJson.value = response.body()
                        runBlocking {
                            response.body()?.data?.let { pref.saveUser(it) }
                        }
                    } else {
                        responseJson.value = GeneralResponse(
                            message = response.code().toString(),
                            status = false
                        )
                    }
                }

                override fun onFailure(call: Call<GeneralResponse<UserModel>>, t: Throwable) {
                    _isLoading.value = false
                    responseJson.value = GeneralResponse(
                        message = "500",
                        status = false
                    )
                }
            }
        )
        return responseJson
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreferences)
            }.also { instance = it }
    }
}