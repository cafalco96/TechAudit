package com.example.techaudit.data

import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditLab
import com.example.techaudit.network.AuditApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class AuditRepository(private val auditDao: AuditDao, private val apiService: AuditApiService) {
    val allItems: Flow<List<AuditItem>> = auditDao.getAllItems()
    val allLabs: Flow<List<AuditLab>> = auditDao.getAllLabs()

    suspend fun insert(item: AuditItem) {
        auditDao.insert(item)
    }

    suspend fun update(item: AuditItem) {
        auditDao.update(item)
    }

    suspend fun delete(item: AuditItem) {
        auditDao.delete(item)
    }

    suspend fun insertLab(lab: AuditLab) {
        auditDao.insertLab(lab)
    }

    suspend fun updateLab(lab: AuditLab) {
        auditDao.updateLab(lab)
    }

    suspend fun deleteLab(lab: AuditLab) {
        auditDao.deleteLab(lab)
    }

    suspend fun syncWithApi(): Result<Unit> {
        return try {
            val items = allItems.first()
            if (items.isEmpty()) return Result.success(Unit)

            var allSuccess = true
            items.forEach { item ->
                val response = apiService.syncItem(item)
                if (!response.isSuccessful) {
                    allSuccess = false
                }
            }

            if (allSuccess) Result.success(Unit)
            else Result.failure(Exception("Error al sincronizar algunos elementos"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
