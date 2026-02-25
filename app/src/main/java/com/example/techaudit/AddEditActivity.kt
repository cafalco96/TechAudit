package com.example.techaudit

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.techaudit.databinding.ActivityAddEditBinding
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditStatus
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class AddEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditBinding

    private var itemEditar: AuditItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Detectar modo edicion
        if(intent.hasExtra("EXTRA_ITEM_EDITAR")) {
            //recuperar elemento
            itemEditar = if (android.os.Build.VERSION.SDK_INT >= 33) {
                intent.getParcelableExtra("EXTRA_ITEM_EDITAR", AuditItem::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra("EXTRA_ITEM_EDITAR")
            }
        }
        //Llenar campos de texto
        itemEditar?.let {
            item -> binding.etNombre.setText(item.nombre)
            binding.etUbicacion.setText(item.ubicacion)
            binding.etNotas.setText(item.notas)

            //Seleccionar spinner
            val posicionSpinner = AuditStatus.values().indexOf(item.estado)
            binding.spEstado.setSelection(posicionSpinner)
        }
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupSpinner()
        binding.btnGuardar.setOnClickListener {
            guardarOActualizar()
        }
    }
    private fun setupSpinner() {
        // Truco: Convertimos el Enum a una lista de Strings para el Spinner
        val estados = AuditStatus.values() // [PENDIENTE, OPERATIVO, ...]

        val adapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item,
            estados
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spEstado.adapter = adapter
    }

    private fun guardarOActualizar() {
        // A. Capturar textos
        val nombre = binding.etNombre.text.toString()
        val ubicacion = binding.etUbicacion.text.toString()
        val notas = binding.etNotas.text.toString()

        // B. Validar (Regla de Negocio)
        if (nombre.isBlank() || ubicacion.isBlank()) {
            Toast.makeText(this, "Nombre y Ubicación son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        // C. Obtener el Estado seleccionado del Spinner
        // El Spinner nos da la posición (0, 1, 2...), la usamos para buscar en el Enum
        val estadoSeleccionado = binding.spEstado.selectedItem as AuditStatus
        val database = (application as TechAuditApp).database

        lifecycleScope.launch {
            if (itemEditar == null) {
                val nuevoItem = AuditItem(
                    id = UUID.randomUUID().toString(),
                    nombre = nombre,
                    ubicacion = ubicacion,
                    fechaRegistro = Date().toString(),
                    estado = estadoSeleccionado,
                    notas = notas
                )
                database.auditDao().insert(nuevoItem)
                Toast.makeText(this@AddEditActivity, "Equipo Agregado", Toast.LENGTH_SHORT).show()
            } else {
                val itemActualizado = itemEditar!!.copy(
                    nombre = nombre,
                    ubicacion = ubicacion,
                    estado = estadoSeleccionado,
                    notas = notas
                )
                database.auditDao().update(itemActualizado)
                Toast.makeText(this@AddEditActivity, "Equipo Actualizado", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}