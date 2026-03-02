package com.example.techaudit.data

import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditLab
import kotlinx.coroutines.flow.Flow

class AuditRepository(private val auditDao: AuditDao) {
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

    suspend fun deleteLab(lab: AuditLab) {
        auditDao.deleteLab(lab)
    }
}
