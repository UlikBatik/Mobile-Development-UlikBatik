package com.example.ulikbatik.di

import android.content.Context
import com.example.ulikbatik.data.local.UserPreferences
import com.example.ulikbatik.data.local.dataStore
import com.example.ulikbatik.data.remote.config.ApiConfig
import com.example.ulikbatik.data.repository.AuthRepository
import com.example.ulikbatik.data.repository.PostRepository
import com.example.ulikbatik.data.repository.ScanRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiInstance("")
        return AuthRepository.getInstance(apiService, pref)
    }

    fun providePostRepository(context: Context): PostRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken().first() }
        val apiService = ApiConfig.getApiInstance(user)
        return PostRepository.getInstance(apiService)
    }

    fun provideScanRepository(context: Context): ScanRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken().first() }
        val apiService = ApiConfig.getApiInstance(user)
        return ScanRepository.getInstance(apiService)
    }
}