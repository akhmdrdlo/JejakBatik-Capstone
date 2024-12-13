package com.example.jejak_batik.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.jejak_batik.data.pref.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull

class SplashViewModel(private val userPreference: UserPreference) : ViewModel() {

    fun getToken() = liveData(Dispatchers.IO) {
        val token = userPreference.getToken().firstOrNull()
        emit(token)
    }

    fun clearSession() = liveData(Dispatchers.IO) {
        try {
            userPreference.clearSession()
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }

}
