package com.example.techaudit.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit.databinding.ItemAuditBinding

import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditStatus

class AuditAdapter (
    val listaCategoria: MutableList<AuditItem>,
    private val onItemSelected: (AuditItem) -> Unit
) : RecyclerView.Adapter<AuditAdapter.AuditViewHolder>() {
    inner class AuditViewHolder(val binding: ItemAuditBinding) :
        RecyclerView.ViewHolder(binding.root)

    //Crear el molde -> ocurre pocas veces
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuditViewHolder {
        val binding = ItemAuditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AuditViewHolder(binding)
    }

    //Cuantos datos tengo
    override fun getItemCount(): Int {
        return listaCategoria.size
    }

    fun actualizarLista(nuevaLista: List<AuditItem>) {
        listaCategoria.clear()
        listaCategoria.addAll(nuevaLista)
        notifyDataSetChanged() // Refrescar la pantalla
    }

    //Pintar los datos -> ocurre muchas veces - cada scroll
    override fun onBindViewHolder(holder: AuditViewHolder, position: Int) {
        val item = listaCategoria[position]

        //Asignar textos
        holder.binding.tvNombreEquipo.text = item.nombre
        holder.binding.tvUbicacion.text = item.ubicacion
        holder.binding.tvEstadoLabel.text = item.estado.name

        //Logica visual que cambia colores
        val colorEstado = when (item.estado) {
            AuditStatus.PENDIENTE -> Color.parseColor("#FFC107")
            AuditStatus.OPERATIVO -> Color.parseColor("#4CAF50")
            AuditStatus.DANIADO -> Color.parseColor("#F44336")
            AuditStatus.NO_ENCONTRADO -> Color.BLACK

        }
        //Pintar barra lateral y texto
        holder.binding.viewStatusColor.setBackgroundColor(colorEstado)
        holder.binding.tvEstadoLabel.setTextColor(colorEstado)

        //Configurar el clic en las tarjetas
        holder.itemView.setOnClickListener {
            onItemSelected(item) //Devuelve el obj seleccionado al activity
        }
    }


}