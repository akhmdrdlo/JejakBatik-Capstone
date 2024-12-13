package com.example.jejak_batik.data.model.auth

import com.google.gson.annotations.SerializedName

data class TokenRequest(
    @SerializedName("kode_token") val token: String
)
