package com.example.jejak_batik.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.jejak_batik.data.model.common.BaseResponse
import com.example.jejak_batik.data.pref.UserRepository
import com.example.jejak_batik.di.Result
import com.google.gson.Gson
import retrofit2.HttpException

class SignupViewModel(private val repository: UserRepository) : ViewModel() {

    fun register(name: String, email: String, password: String): LiveData<Result<BaseResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = repository.register(name, email, password)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val errorResponse = parseError(e)
                emit(Result.Error(errorResponse.message))
            } catch (e: java.net.ConnectException) {
                emit(Result.Error("Tidak dapat terhubung ke server."))
            } catch (e: Exception) {
                emit(Result.Error("Terjadi kesalahan yang tidak terduga: ${e.message}"))
            }
        }
    }

    private fun parseError(e: HttpException): BaseResponse {
        return try {
            val errorJsonString = e.response()?.errorBody()?.string()
            if (!errorJsonString.isNullOrEmpty()) {
                Gson().fromJson(errorJsonString, BaseResponse::class.java)
            } else {
                BaseResponse(false, "Unknown error occurred")
            }
        } catch (jsonException: Exception) {
            BaseResponse(false, "Unexpected response: ${e.response()?.errorBody()?.string()}")
        }
    }
