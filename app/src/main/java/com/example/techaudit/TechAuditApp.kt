package com.example.techaudit

import android.app.Application
import com.example.techaudit.data.AuditDatabase

class TechAuditApp: Application() {
    //base de datos se crea solo cuando alguien la pide por primera vez
    val database by lazy { AuditDatabase.getDatabase(this) }

}