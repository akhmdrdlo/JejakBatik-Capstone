package com.example.jejak_batik.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.jejak_batik.data.model.auth.LoginResponse
import com.example.jejak_batik.data.pref.UserRepository
import com.example.jejak_batik.di.Result
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = repository.login(email, password)
            Log.d("LoginViewModel", "Response dari backend: $response")

            if (response.status) {
                saveToken(response.nama, response.email, response.kode_token)
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message ?: "Login gagal. Silakan cek email dan password Anda."))
            }
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Error: ${e.message}")
            emit(Result.Error("Terjadi kesalahan. Silakan coba lagi."))
        }
    }

    fun saveToken(name: String, email: String, token: String) {
        viewModelScope.launch {
            repository.saveToken(name, email, token)
        }
    }
}

