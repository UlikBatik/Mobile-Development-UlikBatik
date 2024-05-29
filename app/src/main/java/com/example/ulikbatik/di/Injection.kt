package com.example.ulikbatik.di

import android.content.Context
import com.example.ulikbatik.data.remote.config.ApiConfig
import com.example.ulikbatik.data.repository.AuthRepository

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        //disini add datastore buat login ketika save session
        val apiService = ApiConfig.getApiInstance("")
        return AuthRepository.getInstance(apiService)
    }
}