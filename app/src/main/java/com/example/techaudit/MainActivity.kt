package com.example.techaudit

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
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

    private fun setupRecyclerView() {
        adapter = LabAdapter(emptyList()) { lab ->
            val intent = Intent(this, AuditListActivity::class.java)
            intent.putExtra("EXTRA_LAB_ID", lab.id)
            intent.putExtra("EXTRA_LAB_NOMBRE", lab.nombre)
            startActivity(intent)
        }
        binding.rvLaboratorios.adapter = adapter
        binding.rvLaboratorios.layoutManager = LinearLayoutManager(this)
    }
}
