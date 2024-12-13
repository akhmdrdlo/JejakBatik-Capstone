package com.example.jejak_batik.data.model.common

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String
)


