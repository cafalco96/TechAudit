package com.example.techaudit

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit.adapter.AuditAdapter
import com.example.techaudit.databinding.ActivityAuditListBinding
import com.example.techaudit.ui.AuditViewModel

class AuditListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuditListBinding
    private val viewModel: AuditViewModel by viewModels()
    private lateinit var adapter: AuditAdapter
    private var labId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuditListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        labId = intent.getStringExtra("EXTRA_LAB_ID")
        val labNombre = intent.getStringExtra("EXTRA_LAB_NOMBRE")
        
        supportActionBar?.title = labNombre ?: "Equipos"

        setupRecyclerView()
        configurarDeslizarParaBorrar()

        labId?.let { id ->
            viewModel.getItemsByLab(id).observe(this) { items ->
                adapter.actualizarLista(items)
            }
        }

        binding.fabAgregar.setOnClickListener {
            val intent = Intent(this, AddEditActivity::class.java)
            intent.putExtra("EXTRA_LAB_ID", labId)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupRecyclerView() {
        adapter = AuditAdapter(mutableListOf()) { itemSeleccionado ->
            val intent = Intent(this, AddEditActivity::class.java)
            intent.putExtra("EXTRA_ITEM_EDITAR", itemSeleccionado)
            intent.putExtra("EXTRA_LAB_ID", labId)
            startActivity(intent)
        }
        binding.rvAuditoria.adapter = adapter
        binding.rvAuditoria.layoutManager = LinearLayoutManager(this)
    }

    private fun configurarDeslizarParaBorrar() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(r: RecyclerView, v: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder): Boolean = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.listaCategoria[viewHolder.adapterPosition]
                viewModel.delete(item)
                Toast.makeText(this@AuditListActivity, "Equipo eliminado", Toast.LENGTH_SHORT).show()
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.rvAuditoria)
    }
}
