package com.example.jejak_batik.data.model.auth

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("otp")
	val otp: String? = null
)
