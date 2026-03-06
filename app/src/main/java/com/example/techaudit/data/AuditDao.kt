package com.example.techaudit.data

import androidx.room.*
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditLab
import kotlinx.coroutines.flow.Flow

@Dao
interface AuditDao {
    // Equipos
    @Query("SELECT * FROM equipos ORDER BY fechaRegistro DESC")
    fun getAllItems(): Flow<List<AuditItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AuditItem)

    @Update
    suspend fun update(item: AuditItem)

    @Delete
    suspend fun delete(item: AuditItem)

    // Laboratorios
    @Query("SELECT * FROM laboratorios ORDER BY nombre ASC")
    fun getAllLabs(): Flow<List<AuditLab>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLab(lab: AuditLab)

    @Update
    suspend fun updateLab(lab: AuditLab)

    @Delete
    suspend fun deleteLab(lab: AuditLab)
}
