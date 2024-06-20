package com.example.ulikbatik.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ulikbatik.data.repository.CatalogRepository
import com.example.ulikbatik.data.repository.ScrapRepository
import com.example.ulikbatik.di.Injection
import com.example.ulikbatik.ui.catalog.CatalogViewModel

class CatalogViewModelFactory(
    private val catalogRepository: CatalogRepository,
    private val scrapperRepository: ScrapRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when (modelClass) {
            CatalogViewModel::class.java -> return CatalogViewModel(catalogRepository, scrapperRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }


    companion object {
        @Volatile
        private var instance: CatalogViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): CatalogViewModelFactory {
            instance = CatalogViewModelFactory(
                Injection.provideCatalogRepository(context),
                Injection.provideScrapRepository(context)
            )
            return instance as CatalogViewModelFactory
        }
    }
}