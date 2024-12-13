package com.example.jejak_batik.data.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("user_token")
        private val USER_ID_KEY = intPreferencesKey("user_id")
        private val NAME_KEY = stringPreferencesKey("user_name")
        private val EMAIL_KEY = stringPreferencesKey("user_email")


        @Volatile
        private var instance: UserPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return instance ?: synchronized(this) {
                instance ?: UserPreference(dataStore)
            }.also { instance = it }
        }
    }

    suspend fun saveToken(name: String, email: String, token: String) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = name
            preferences[EMAIL_KEY] = email
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    fun getEmail(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[EMAIL_KEY]
        }
    }

    fun getName(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[NAME_KEY]
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                userId = preferences[USER_ID_KEY] ?: 0,
                name = preferences[NAME_KEY] ?: "",
                email = preferences[EMAIL_KEY] ?: "",
                token = preferences[TOKEN_KEY] ?: "",
                isLogin = preferences[TOKEN_KEY] != null
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }


}
