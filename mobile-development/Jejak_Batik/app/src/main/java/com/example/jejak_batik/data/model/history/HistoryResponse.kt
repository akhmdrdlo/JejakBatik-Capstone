package com.example.jejak_batik.data.model.history

data class HistoryResponse(
    val status: Boolean,
    val message: String,
    val data: List<HistoryItem>?
)

