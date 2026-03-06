package com.example.techaudit

import android.app.Application
import com.example.techaudit.data.AuditDatabase
import com.example.techaudit.network.AuditApiService

class TechAuditApp: Application() {
    val database by lazy { AuditDatabase.getDatabase(this) }
    val apiService by lazy { AuditApiService.create() }
}
