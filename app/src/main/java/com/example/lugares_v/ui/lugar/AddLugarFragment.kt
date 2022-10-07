package com.example.lugares_v.ui.lugar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.lugares_v.R
import com.example.lugares_v.databinding.FragmentAddLugarBinding
import com.example.lugares_v.model.Lugar
import com.example.lugares_v.viewmodel.LugarViewModel

class AddLugarFragment : Fragment() {
    private var _binding: FragmentAddLugarBinding? = null
    private val binding get() = _binding!!
    private lateinit var lugarViewModel: LugarViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel = ViewModelProvider(this).get(LugarViewModel::class.java)

        _binding = FragmentAddLugarBinding.inflate(inflater, container, false)

        binding.btAddLugar.setOnClickListener { addLugar() }

        return binding.root
    }
    //Efectivamente hace el registro del lugar en la base de datos
    private fun addLugar() {
        val nombre = binding.etNombre.text.toString()
        val correo = binding.etCorreoLugar.text.toString()
        val telefono = binding.etTelefono.text.toString()
        val web = binding.etWeb.text.toString()
        if (nombre.isNotEmpty()) { //Al menos tenemos un nombre
            val lugar = Lugar(0, nombre, correo, telefono, web, 0.0, 0.0, 0.0, "", "")
            lugarViewModel.saveLugar(lugar)
            Toast.makeText(requireContext(),getString(R.string.msg_lugar_added),
            Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addLugarFragment_to_nav_lugar)
        } else { //No hay información del lugar...
            Toast.makeText(requireContext(),getString(R.string.msg_data),
                Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}