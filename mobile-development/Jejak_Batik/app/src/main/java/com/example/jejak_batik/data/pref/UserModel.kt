package com.example.jejak_batik.data.pref

data class UserModel(
    val userId: Int = 0,
    val name: String = "",
    val email: String = "",
    val token: String = "",
    val isLogin: Boolean = false
)

