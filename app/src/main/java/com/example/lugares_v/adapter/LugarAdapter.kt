package com.example.lugares_v.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.lugares_v.databinding.LugarFilaBinding
import com.example.lugares_v.model.Lugar
import com.example.lugares_v.ui.lugar.LugarFragmentDirections

class LugarAdapter : RecyclerView.Adapter<LugarAdapter.LugarViewHolder>() {
    //La lista de lugares a "dibujar"
    private var listaLugares = emptyList<Lugar>()
    //Contenedor de vistas "cajitas" en memoria...
    inner class LugarViewHolder(private val itemBinding: LugarFilaBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun dibuja(lugar: Lugar) {
            itemBinding.tvNombre.text = lugar.nombre
            itemBinding.tvTelefono.text = lugar.telefono
            itemBinding.tvCorreo.text = lugar.correo
            itemBinding.vistaFila.setOnClickListener {
                val accion =  LugarFragmentDirections
                    .actionNavLugarToUpdateLugarFragment(lugar)
                itemView.findNavController().navigate(accion)
            }
        }
    }
    //Crea un "cajita" una vista del tipo lugarFila...
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {
        val itemBinding = LugarFilaBinding
            .inflate(LayoutInflater.from(parent.context)
                ,parent
                ,false)
        return LugarViewHolder(itemBinding)
    }
    //Con una "cajita" creada... se pasa a dibujar los datos del lugar x
    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {
        val lugarActual = listaLugares[position]
        holder.dibuja(lugarActual)
    }
    override fun getItemCount(): Int {
        return listaLugares.size
    }
    fun setLugares(lugares : List<Lugar>) {
        listaLugares = lugares
        notifyDataSetChanged()  //Se notifica que el conjunto de datos cambio y se redibuja toda la lista
    }
}