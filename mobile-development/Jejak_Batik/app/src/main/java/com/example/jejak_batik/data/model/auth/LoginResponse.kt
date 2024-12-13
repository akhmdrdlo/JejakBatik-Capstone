package com.example.jejak_batik.data.model.auth

data class LoginResponse(
    val userId: Int,
    val email: String,
    val kode_token: String,
    val nama: String,
    val status: Boolean,
    val message: String
)







