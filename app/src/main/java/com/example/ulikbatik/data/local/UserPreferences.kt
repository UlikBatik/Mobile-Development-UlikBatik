package com.example.ulikbatik.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences private constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val TOKEN_KEY = stringPreferencesKey("token_key")
    private val USER_ID = stringPreferencesKey("user_id")
    private val SESSION_BOARDING = booleanPreferencesKey("session_boarding")

    fun getUserToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    suspend fun saveTokenUser(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    fun getUserId(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[USER_ID]
        }
    }

    suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    suspend fun deleteSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun getSession(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[SESSION_BOARDING] ?: false
        }
    }

    suspend fun saveSession(loginSession: Boolean) {
        dataStore.edit { preferences ->
            preferences[SESSION_BOARDING] = loginSession
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}