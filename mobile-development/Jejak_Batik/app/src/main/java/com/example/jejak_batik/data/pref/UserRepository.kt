package com.example.jejak_batik.data.pref


import com.example.jejak_batik.data.api.ApiService
import com.example.jejak_batik.data.model.common.BaseResponse
import com.example.jejak_batik.data.model.auth.LoginResponse
import com.example.jejak_batik.data.model.auth.RegisterRequest
import com.example.jejak_batik.data.model.auth.VerifyRequest
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.runBlocking

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService

) {
    suspend fun register(name: String, email: String, password: String): BaseResponse {
        val request = RegisterRequest(name, email, password)
        return apiService.register(request)
    }

    suspend fun verify(email: String, otp: String, name: String, password: String): BaseResponse {
        val request = VerifyRequest(email, otp, name, password)
        return apiService.verify(request)
    }

    suspend fun resendOtp(email: String): BaseResponse {
        return apiService.resendOtp(email)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        val request = ApiService.LoginRequest(email, password)
        return apiService.login(request)
    }


    fun saveToken(name: String, email: String, token: String) {
        runBlocking {
            userPreference.saveToken(name, email, token)
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}
