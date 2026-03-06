package com.example.techaudit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.techaudit.databinding.ActivityAddLabBinding
import com.example.techaudit.model.AuditLab
import com.example.techaudit.ui.AuditViewModel
import java.util.UUID

class AddLabActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddLabBinding
    private val viewModel: AuditViewModel by viewModels()
    private var labAEditar: AuditLab? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddLabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        labAEditar = intent.getParcelableExtra("EXTRA_LAB")
        
        if (labAEditar != null) {
            binding.etNombreLab.setText(labAEditar?.nombre)
            binding.etEdificioLab.setText(labAEditar?.edificio)
            binding.btnGuardarLab.text = "Actualizar Laboratorio"
        }

        binding.btnGuardarLab.setOnClickListener {
            guardarLab()
        }
    }

    private fun guardarLab() {
        val nombre = binding.etNombreLab.text.toString()
        val edificio = binding.etEdificioLab.text.toString()

        if (nombre.isBlank() || edificio.isBlank()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (labAEditar != null) {
            val labActualizado = labAEditar!!.copy(nombre = nombre, edificio = edificio)
            viewModel.updateLab(labActualizado)
            Toast.makeText(this, "Laboratorio Actualizado", Toast.LENGTH_SHORT).show()
        } else {
            val nuevoLab = AuditLab(
                id = UUID.randomUUID().toString(),
                nombre = nombre,
                edificio = edificio
            )
            viewModel.insertLab(nuevoLab)
            Toast.makeText(this, "Laboratorio Guardado", Toast.LENGTH_SHORT).show()
        }
        finish()
    }
}
