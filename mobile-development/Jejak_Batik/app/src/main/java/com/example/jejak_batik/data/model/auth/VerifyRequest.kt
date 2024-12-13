package com.example.jejak_batik.data.model.auth

data class VerifyRequest(
    val email: String,
    val otp: String,
    val nama: String,
    val password: String
)
