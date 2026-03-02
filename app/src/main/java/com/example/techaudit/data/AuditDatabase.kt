package com.example.techaudit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditLab

@Database(entities = [AuditItem::class, AuditLab::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AuditDatabase : RoomDatabase() {
    abstract fun auditDao(): AuditDao

    companion object {
        @Volatile
        private var INSTANCE: AuditDatabase? = null

        fun getDatabase(context: Context): AuditDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AuditDatabase::class.java,
                    "techaudit_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
