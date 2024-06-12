package com.example.ulikbatik.ui.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ulikbatik.data.model.BatikModel
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.data.remote.response.ResultResponse
import com.example.ulikbatik.data.remote.response.ScrapperResponse
import com.example.ulikbatik.data.repository.CatalogRepository
import com.example.ulikbatik.data.repository.ScrapRepository

class CatalogViewModel(
    private val catalogRepository: CatalogRepository,
    private val scrapRepository: ScrapRepository

) : ViewModel() {

    val isLoading = catalogRepository.isLoading
    val isLoadingProduct = scrapRepository.isLoading

    val dataCatalogs = catalogRepository.getCatalog()

    fun getDetailCatalog(idBatik: String): LiveData<GeneralResponse<BatikModel>> {
        return catalogRepository.detailCatalog(idBatik)
    }

    fun searchCatalog(query: String): LiveData<GeneralResponse<List<BatikModel>>> {
        return catalogRepository.searchCatalog(query)
    }

    fun getScrapData(batikName: String): LiveData<ResultResponse<List<ScrapperResponse>>> {
        return scrapRepository.getScrapperData(batikName)
    }

}