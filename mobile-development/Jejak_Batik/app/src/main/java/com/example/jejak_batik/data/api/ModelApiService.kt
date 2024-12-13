package com.example.jejak_batik.data.api

import com.example.jejak_batik.data.model.catalog.CatalogResponse
import com.example.jejak_batik.data.model.history.HistoryResponse
import com.example.jejak_batik.data.model.predict.PredictResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ModelApiService {

    @Multipart
    @POST("predict")
    fun predict(
        @Part("email") email: RequestBody,
        @Part img: MultipartBody.Part
    ): Call<PredictResponse>

    @GET("predict/histories")
    fun getHistories(
        @Query("email") email: String
    ): Call<HistoryResponse>

    @GET("catalog")
    fun getCatalog(): Call<CatalogResponse>

}


