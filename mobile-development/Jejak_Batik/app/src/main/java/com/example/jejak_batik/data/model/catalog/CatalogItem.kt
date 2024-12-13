package com.example.jejak_batik.data.model.catalog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CatalogItem(
    val name: String,
    val description: String,
    val link_image: String,
    val link_shop: String,
    val occasion: String,
    val history: String
) : Parcelable

