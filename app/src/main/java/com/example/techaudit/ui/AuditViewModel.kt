package com.example.techaudit.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.techaudit.TechAuditApp
import com.example.techaudit.data.AuditRepository
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditLab
import kotlinx.coroutines.launch

class AuditViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AuditRepository
    val allItems: LiveData<List<AuditItem>>
    val allLabs: LiveData<List<AuditLab>>

    private val _syncStatus = MutableLiveData<SyncStatus>()
    val syncStatus: LiveData<SyncStatus> = _syncStatus

    init {
        val app = application as TechAuditApp
        val dao = app.database.auditDao()
        val apiService = app.apiService
        repository = AuditRepository(dao, apiService)
        allItems = repository.allItems.asLiveData()
        allLabs = repository.allLabs.asLiveData()
    }

    fun syncData() = viewModelScope.launch {
        _syncStatus.value = SyncStatus.Loading
        val result = repository.syncWithApi()
        if (result.isSuccess) {
            _syncStatus.value = SyncStatus.Success("Sincronización completada con éxito")
        } else {
            _syncStatus.value = SyncStatus.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
        }
    }

    fun resetSyncStatus() {
        _syncStatus.value = SyncStatus.Idle
    }

    fun getItemsByLab(labId: String): LiveData<List<AuditItem>> {
        return allItems.map { items ->
            items.filter { it.laboratorioId == labId }
        }
    }

    fun insertLab(lab: AuditLab) = viewModelScope.launch {
        repository.insertLab(lab)
    }

    fun updateLab(lab: AuditLab) = viewModelScope.launch {
        repository.updateLab(lab)
    }

    fun deleteLab(lab: AuditLab) = viewModelScope.launch {
        repository.deleteLab(lab)
    }

    fun insert(item: AuditItem) = viewModelScope.launch {
        repository.insert(item)
    }

    fun update(item: AuditItem) = viewModelScope.launch {
        repository.update(item)
    }

    fun delete(item: AuditItem) = viewModelScope.launch {
        repository.delete(item)
    }

    sealed class SyncStatus {
        object Idle : SyncStatus()
        object Loading : SyncStatus()
        data class Success(val message: String) : SyncStatus()
        data class Error(val message: String) : SyncStatus()
    }
}
