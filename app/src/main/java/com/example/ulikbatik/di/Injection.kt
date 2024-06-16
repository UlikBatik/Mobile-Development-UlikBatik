package com.example.ulikbatik.di

import android.content.Context
import com.example.ulikbatik.data.local.UserPreferences
import com.example.ulikbatik.data.local.dataStore
import com.example.ulikbatik.data.model.UserModel
import com.example.ulikbatik.data.remote.config.ApiConfig
import com.example.ulikbatik.data.repository.AuthRepository
import com.example.ulikbatik.data.repository.CatalogRepository
import com.example.ulikbatik.data.repository.LikesRepository
import com.example.ulikbatik.data.repository.PostRepository
import com.example.ulikbatik.data.repository.ScanRepository
import com.example.ulikbatik.data.repository.ScrapRepository
import com.example.ulikbatik.data.repository.UserRepository
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
        val userModel = runBlocking { pref.getUser().first() }
        val apiService = ApiConfig.getApiInstance(user)
        return PostRepository.getInstance(apiService, userModel!!)
    }

    fun provideLikesRepository(context: Context): LikesRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken().first() }
        val apiService = ApiConfig.getApiInstance(user)
        return LikesRepository.getInstance(apiService)
    }

    fun provideScanRepository(context: Context): ScanRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken().first() }
        val apiService = ApiConfig.getApiInstance(user)
        return ScanRepository.getInstance(apiService)
    }

    fun provideCatalogRepository(context: Context): CatalogRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken().first() }
        val apiService = ApiConfig.getApiInstance(user)
        return CatalogRepository.getInstance(apiService)
    }

    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken().first() }
        val apiService = ApiConfig.getApiInstance(user)
        return UserRepository.getInstance(apiService, pref)
    }

    fun providePreferences(context: Context): UserPreferences {
        val pref = UserPreferences.getInstance(context.dataStore)
        return pref
    }

    fun provideUserModel(context: Context): UserModel? {
        val pref = UserPreferences.getInstance(context.dataStore)
        val userModel = runBlocking { pref.getUser().first() }
        return userModel
    }

    fun provideScrapRepository(context: Context): ScrapRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken().first() }
        val apiService = ApiConfig.getApiInstance(user)
        return ScrapRepository.getInstance(apiService)
    }
}