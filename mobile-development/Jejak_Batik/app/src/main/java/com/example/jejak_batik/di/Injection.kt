package com.example.jejak_batik.di

import android.content.Context
import com.example.jejak_batik.data.api.ApiConfig
import com.example.jejak_batik.data.api.ModelApiConfig
import com.example.jejak_batik.data.pref.ModelRepository
import com.example.jejak_batik.data.pref.UserPreference
import com.example.jejak_batik.data.pref.UserRepository
import com.example.jejak_batik.data.pref.dataStore

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(context)
        return UserRepository.getInstance(userPreference, apiService)
    }

    fun provideModelRepository(): ModelRepository {
        val apiService = ModelApiConfig.getApiService()
        return ModelRepository(apiService)
    }
}
