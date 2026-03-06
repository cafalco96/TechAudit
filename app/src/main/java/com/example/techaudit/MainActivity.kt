package com.example.techaudit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit.adapter.LabAdapter
import com.example.techaudit.databinding.ActivityMainBinding
import com.example.techaudit.ui.AuditViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: AuditViewModel by viewModels()
    private lateinit var adapter: LabAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSync()

        viewModel.allLabs.observe(this) { labs ->
            adapter.updateList(labs)
        }

        binding.fabAgregarLab.setOnClickListener {
            val intent = Intent(this, AddLabActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupSync() {
        binding.btnSincronizar.setOnClickListener {
            viewModel.syncData()
        }

        viewModel.syncStatus.observe(this) { status ->
            when (status) {
                is AuditViewModel.SyncStatus.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSincronizar.isEnabled = false
                }
                is AuditViewModel.SyncStatus.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSincronizar.isEnabled = true
                    Toast.makeText(this, status.message, Toast.LENGTH_SHORT).show()
                    viewModel.resetSyncStatus()
                }
                is AuditViewModel.SyncStatus.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSincronizar.isEnabled = true
                    Toast.makeText(this, "Error: ${status.message}", Toast.LENGTH_LONG).show()
                    viewModel.resetSyncStatus()
                }
                AuditViewModel.SyncStatus.Idle -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSincronizar.isEnabled = true
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = LabAdapter(
            list = emptyList(),
            onItemSelected = { lab ->
                val intent = Intent(this, AuditListActivity::class.java)
                intent.putExtra("EXTRA_LAB_ID", lab.id)
                intent.putExtra("EXTRA_LAB_NOMBRE", lab.nombre)
                startActivity(intent)
            },
            onItemLongClicked = { lab ->
                val intent = Intent(this, AddLabActivity::class.java)
                intent.putExtra("EXTRA_LAB", lab)
                startActivity(intent)
            }
        )
        binding.rvLaboratorios.adapter = adapter
        binding.rvLaboratorios.layoutManager = LinearLayoutManager(this)

        // Implementación de Swipe para eliminar
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val lab = adapter.getLabAt(position)
                
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Eliminar Laboratorio")
                    .setMessage("¿Estás seguro de eliminar el laboratorio '${lab.nombre}'? Se eliminarán todos sus equipos asociados.")
                    .setPositiveButton("Eliminar") { _, _ ->
                        viewModel.deleteLab(lab)
                        Toast.makeText(this@MainActivity, "Laboratorio eliminado", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancelar") { _, _ ->
                        adapter.notifyItemChanged(position)
                    }
                    .show()
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rvLaboratorios)
    }
}
