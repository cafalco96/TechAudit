package com.example.techaudit.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "laboratorios")
@Parcelize
data class AuditLab(
    @PrimaryKey
    val id: String,
    val nombre: String,
    val edificio: String
) : Parcelable
