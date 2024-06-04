package com.example.ulikbatik.ui.scan

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ulikbatik.data.repository.ScanRepository
import com.example.ulikbatik.di.Injection

class ScanViewModelFactory(
    private val scanRepository: ScanRepository
) : ViewModelProvider.Factory {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when (modelClass) {
            ScanViewModel::class.java -> return ScanViewModel(scanRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }


    companion object {
        @Volatile
        private var instance: ScanViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ScanViewModelFactory {
            instance = ScanViewModelFactory(Injection.provideScanRepository(context))
            return instance as ScanViewModelFactory
        }
    }
}