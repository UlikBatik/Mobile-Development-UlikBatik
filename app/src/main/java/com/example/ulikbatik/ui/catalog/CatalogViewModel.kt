package com.example.ulikbatik.ui.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ulikbatik.data.model.BatikModel
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.data.repository.CatalogRepository

class CatalogViewModel(
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    val isLoading = catalogRepository.isLoading

    val dataCatalogs = catalogRepository.getCatalog()

}