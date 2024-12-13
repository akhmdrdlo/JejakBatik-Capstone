package com.example.jejak_batik.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.jejak_batik.data.model.common.BaseResponse
import com.example.jejak_batik.data.pref.UserRepository
import com.example.jejak_batik.di.Result
import com.google.gson.Gson
import retrofit2.HttpException

class VerifyViewModel(private val repository: UserRepository) : ViewModel() {

    fun verify(email: String, otp: String, name: String, password: String): LiveData<Result<BaseResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = repository.verify(email, otp, name, password)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val errorMessage = if (e.code() == 400) {
                    "OTP tidak sesuai. Silakan periksa dan coba lagi."
                } else {
                    e.message ?: "Terjadi kesalahan yang tidak diketahui."
                }
                emit(Result.Error(errorMessage))
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Terjadi kesalahan yang tidak terduga."))
            }
        }


    fun resendOtp(email: String): LiveData<Result<BaseResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = repository.resendOtp(email)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Terjadi kesalahan."))
        }
    }

}

private fun parseError(e: HttpException): BaseResponse {
        val errorJsonString = e.response()?.errorBody()?.string()
        return try {
            Gson().fromJson(errorJsonString, BaseResponse::class.java)
        } catch (exception: Exception) {
            BaseResponse(false, "Unexpected response from server.")
        }
    }



