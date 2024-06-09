package com.example.ulikbatik.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ulikbatik.data.model.UserModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences private constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val TOKEN_KEY = stringPreferencesKey("token_key")
    private val USER_ID = stringPreferencesKey("user_id")
    private val USERNAME = stringPreferencesKey("username")
    private val USER_KEY = stringPreferencesKey("user_key")
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
    suspend fun saveUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME] = username
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


    fun getSession(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[SESSION_BOARDING] ?: false
        }
    }

    suspend fun setSession() {
        dataStore.edit { preferences ->
            preferences[SESSION_BOARDING] = true
        }
    }

    suspend fun logOut() {
        dataStore.edit { preferences ->
            preferences.clear()
            preferences[SESSION_BOARDING] = true
        }
    }

    fun getUser(): Flow<UserModel?> {
        return dataStore.data.map { preferences ->
            val json = preferences[USER_KEY]
            Gson().fromJson<UserModel>(json, UserModel::class.java)
        }
    }

    suspend fun saveUser(user: UserModel) {
        val json = Gson().toJson(user)
        dataStore.edit { preferences ->
            preferences[USER_KEY] = json
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