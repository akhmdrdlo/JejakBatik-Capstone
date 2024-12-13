package com.example.jejak_batik.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_history")
data class LocalHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val createdAt: String,
    val description: String?,
    val occasion: String?,
    val imageUrl: String?
)
