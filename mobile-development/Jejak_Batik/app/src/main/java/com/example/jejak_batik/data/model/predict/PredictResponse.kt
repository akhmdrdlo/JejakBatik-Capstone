package com.example.jejak_batik.data.model.predict

data class PredictResponse(
    val status: Boolean,
    val data: PredictionData?,
    val message: String?
)

data class PredictionData(
    val name: String,
    val description: String,
    val link_shop: String?,
    val occasion: String?
)

