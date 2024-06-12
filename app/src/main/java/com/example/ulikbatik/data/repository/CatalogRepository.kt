package com.example.ulikbatik.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ulikbatik.data.model.BatikModel
import com.example.ulikbatik.data.remote.config.ApiService
import com.example.ulikbatik.data.remote.response.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatalogRepository(
    private val apiService: ApiService
) {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getCatalog(): LiveData<GeneralResponse<List<BatikModel>>> {
        _isLoading.value = true
        val responseJson = MutableLiveData<GeneralResponse<List<BatikModel>>>()
        val client = apiService.getBatik()
        client.enqueue(
            object : Callback<GeneralResponse<List<BatikModel>>> {
                override fun onResponse(
                    call: Call<GeneralResponse<List<BatikModel>>>,
                    response: Response<GeneralResponse<List<BatikModel>>>
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
                    call: Call<GeneralResponse<List<BatikModel>>>,
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

    fun searchCatalog(query: String): LiveData<GeneralResponse<List<BatikModel>>> {
        _isLoading.value = true
        val responseJson = MutableLiveData<GeneralResponse<List<BatikModel>>>()
        val client = apiService.searchBatik(query)
        client.enqueue(
            object : Callback<GeneralResponse<List<BatikModel>>> {
                override fun onResponse(
                    call: Call<GeneralResponse<List<BatikModel>>>,
                    response: Response<GeneralResponse<List<BatikModel>>>
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
                    call: Call<GeneralResponse<List<BatikModel>>>,
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


    fun detailCatalog(idBatik: String): LiveData<GeneralResponse<BatikModel>> {
        _isLoading.value = true
        val responseJson = MutableLiveData<GeneralResponse<BatikModel>>()
        val client = apiService.getDetailBatik(idBatik)
        client.enqueue(
            object : Callback<GeneralResponse<BatikModel>> {
                override fun onResponse(
                    call: Call<GeneralResponse<BatikModel>>,
                    response: Response<GeneralResponse<BatikModel>>
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

                override fun onFailure(call: Call<GeneralResponse<BatikModel>>, t: Throwable) {
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
        private var instance: CatalogRepository? = null
        fun getInstance(
            apiService: ApiService
        ): CatalogRepository =
            instance ?: synchronized(this) {
                instance ?: CatalogRepository(apiService)
            }.also { instance = it }
    }

}

