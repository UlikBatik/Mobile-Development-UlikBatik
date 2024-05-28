package com.example.ulikbatik.data.remote.config

import com.google.gson.GsonBuilder
import de.hdodenhof.circleimageview.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiInstance(token: String?): ApiService {
            val loggingIntercept = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(requestHeaders)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingIntercept)
                .addInterceptor(authInterceptor).build()
            val retrofit =
                Retrofit.Builder().baseUrl("http://localhost:3000/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                    .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}