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

    init {
        val dao = (application as TechAuditApp).database.auditDao()
        repository = AuditRepository(dao)
        allItems = repository.allItems.asLiveData()
        allLabs = repository.allLabs.asLiveData()
    }

    fun getItemsByLab(labId: String): LiveData<List<AuditItem>> {
        return allItems.map { items ->
            items.filter { it.laboratorioId == labId }
        }
    }

    fun insertLab(lab: AuditLab) = viewModelScope.launch {
        repository.insertLab(lab)
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
}
