package com.example.jejak_batik.data.model.history


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryItem(
    val id: String,
    val createdAt: String,
    val imageUrl: String?,
    val result: String,
    val data: NestedData?
) : Parcelable {
    @Parcelize
    data class NestedData(
        val description: String?,
        val occasion: String?
    ) : Parcelable
}
