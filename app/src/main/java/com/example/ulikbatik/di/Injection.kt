package com.example.ulikbatik.di

import android.content.Context
import com.example.ulikbatik.data.remote.config.ApiConfig
import com.example.ulikbatik.data.repository.AuthRepository
import com.example.ulikbatik.data.repository.PostRepository
import com.example.ulikbatik.data.repository.ScanRepository

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        //disini add datastore buat login ketika save session
        val apiService = ApiConfig.getApiInstance("")
        return AuthRepository.getInstance(apiService)
    }

    fun providePostRepository(context: Context): PostRepository {
        //add datastore buat taro token di parameter token dibawah ini
        val apiService =
            ApiConfig.getApiInstance("")
        return PostRepository.getInstance(apiService)
    }

    fun provideScanRepository(context: Context): ScanRepository {
        //add datastore buat taro token di parameter token dibawah ini
        val apiService =
            ApiConfig.getApiInstance("")
        return ScanRepository.getInstance(apiService)
    }
}