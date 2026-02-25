package com.example.techaudit

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.techaudit.adapter.AuditAdapter
import com.example.techaudit.data.AuditDatabase
import com.example.techaudit.databinding.ActivityMainBinding
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditStatus
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AuditAdapter
    private lateinit var database: AuditDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = (application as TechAuditApp).database
        setupRecyclerView()
        cargarDatosdeBaseDeDatos()
        binding.fabAgregar.setOnClickListener {
            insertarRegistro()
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun setupRecyclerView() {
        //Iniciador el adaptador
        val adapter = AuditAdapter(lista) { itemSeleccionado ->
            //Lambda que se ejecuta al dar clic en tarea
            //Toast.makeText(this, itemSeleccionado.nombre, Toast.LENGTH_SHORT).show()
        //Navegar a pantalla de detalle
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("EXTRA_ITEM", itemSeleccionado)
            startActivity(intent)
        }
        binding.rvAuditoria.adapter = adapter
        binding.rvAuditoria.layoutManager = LinearLayoutManager(this)

    }

}