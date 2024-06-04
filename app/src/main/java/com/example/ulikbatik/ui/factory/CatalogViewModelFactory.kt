package com.example.ulikbatik.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ulikbatik.data.Catalog
import com.example.ulikbatik.data.repository.CatalogRepository
import com.example.ulikbatik.di.Injection
import com.example.ulikbatik.ui.catalog.CatalogViewModel
import com.example.ulikbatik.ui.scan.ScanViewModel

class CatalogViewModelFactory(
    private val catalogRepository: CatalogRepository
) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when (modelClass) {
            CatalogViewModel::class.java -> return CatalogViewModel(catalogRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }


    companion object {
        @Volatile
        private var instance: CatalogViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): CatalogViewModelFactory {
            instance = CatalogViewModelFactory(Injection.provideCatalogRepository(context))
            return instance as CatalogViewModelFactory
        }
    }
}