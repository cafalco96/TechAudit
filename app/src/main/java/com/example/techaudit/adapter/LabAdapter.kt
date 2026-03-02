package com.example.techaudit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit.databinding.ItemLabBinding
import com.example.techaudit.model.AuditLab

class LabAdapter(
    private var list: List<AuditLab>,
    private val onItemSelected: (AuditLab) -> Unit
) : RecyclerView.Adapter<LabAdapter.LabViewHolder>() {

    inner class LabViewHolder(val binding: ItemLabBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabViewHolder {
        val binding = ItemLabBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LabViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LabViewHolder, position: Int) {
        val lab = list[position]
        holder.binding.tvNombreLab.text = lab.nombre
        holder.binding.tvEdificioLab.text = lab.edificio
        holder.itemView.setOnClickListener { onItemSelected(lab) }
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<AuditLab>) {
        list = newList
        notifyDataSetChanged()
    }
}
