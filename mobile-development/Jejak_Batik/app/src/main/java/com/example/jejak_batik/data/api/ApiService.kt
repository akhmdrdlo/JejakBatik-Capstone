package com.example.jejak_batik.data.api

import com.example.jejak_batik.data.model.auth.ForgetPasswordRequest
import retrofit2.http.*
import com.example.jejak_batik.data.model.auth.LoginResponse
import com.example.jejak_batik.data.model.auth.LogoutRequest
import com.example.jejak_batik.data.model.auth.LogoutResponse
import com.example.jejak_batik.data.model.auth.RegisterRequest
import com.example.jejak_batik.data.model.auth.VerifyRequest
import com.example.jejak_batik.data.model.common.BaseResponse
import retrofit2.Response


interface ApiService {

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): BaseResponse

    @POST("verify")
    suspend fun verify(@Body request: VerifyRequest): BaseResponse

    @FormUrlEncoded
    @POST("resend-otp")
    suspend fun resendOtp(
        @Field("email") email: String
    ): BaseResponse

    data class LoginRequest(
        val email: String,
        val password: String
    )

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("logout")
    suspend fun logout(@Body logoutRequest: LogoutRequest): Response<LogoutResponse>

    @FormUrlEncoded
    @POST("deleteaccount")
    suspend fun deleteAccount(
        @Field("email") email: String
    ): BaseResponse

    @POST("forgetpassword")
    suspend fun forgetPassword(@Body request: ForgetPasswordRequest): BaseResponse


}

