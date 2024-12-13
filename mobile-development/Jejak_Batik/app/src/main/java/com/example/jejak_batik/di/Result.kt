package com.example.jejak_batik.di

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()

    companion object {
        fun <T> success(data: T): Result<T> = Success(data)
        fun error(message: String): Result<Nothing> = Error(message)
        fun loading(): Result<Nothing> = Loading
    }
}
