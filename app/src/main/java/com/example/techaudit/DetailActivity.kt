package com.example.techaudit

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.techaudit.databinding.ActivityDetailBinding
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditStatus

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Recuerar objeto enviado
        val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_ITEM", AuditItem::class.java)
         }else {
            intent.getParcelableExtra("EXTRA_ITEM")
        }

// Mostrar datos si existe el objeto
        item?.let {
            mostratDetalles(it)

        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun  mostratDetalles(item: AuditItem) {
        binding.tvDetalleNombre.text = item.nombre
        binding.tvDetalleId.text = "ID: ${item.id.substring(0,8)}...."
        binding.tvDetalleUbicacion.text = item.ubicacion
        binding.tvDetalleFecha.text = item.fechaRegistro
        binding.tvDetalleNotas.text = item.notas.ifEmpty { "Sin observaciones." }

        // Color del estado
        val colorEstado = when (item.estado) {
            AuditStatus.PENDIENTE -> Color.parseColor("#FFC107")
            AuditStatus.OPERATIVO -> Color.parseColor("#4CAF50")
            AuditStatus.DANIADO -> Color.parseColor("#F44336")
            AuditStatus.NO_ENCONTRADO -> Color.BLACK
        }
        binding.viewHeaderStatus.setBackgroundColor(colorEstado)
        title = "Detalle: ${item.estado}"
    }
}