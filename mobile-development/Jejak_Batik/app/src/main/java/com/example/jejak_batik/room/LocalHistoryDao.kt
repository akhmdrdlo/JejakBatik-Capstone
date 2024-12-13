package com.example.jejak_batik.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocalHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: LocalHistoryEntity)

    @Query("SELECT * FROM local_history ORDER BY createdAt DESC")
    fun getAllHistory(): LiveData<List<LocalHistoryEntity>>

    @Query("SELECT * FROM local_history ORDER BY createdAt DESC")
    fun getAllSync(): List<LocalHistoryEntity>
}
