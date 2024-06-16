package com.example.ulikbatik.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ulikbatik.data.repository.CatalogRepository
import com.example.ulikbatik.data.repository.ScanRepository
import com.example.ulikbatik.di.Injection
import com.example.ulikbatik.ui.scan.ScanViewModel

class ScanViewModelFactory(
    private val scanRepository: ScanRepository,
    private val catalogRepository: CatalogRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when (modelClass) {
            ScanViewModel::class.java -> return ScanViewModel(scanRepository,catalogRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }


    companion object {
        @Volatile
        private var instance: ScanViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ScanViewModelFactory {
            instance = ScanViewModelFactory(Injection.provideScanRepository(context), Injection.provideCatalogRepository(context))
            return instance as ScanViewModelFactory
        }
    }
}