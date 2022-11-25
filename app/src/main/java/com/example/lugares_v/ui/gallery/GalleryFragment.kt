package com.example.lugares_v.ui.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lugares_v.databinding.FragmentGalleryBinding
import com.example.lugares_v.model.Lugar
import com.example.lugares_v.viewmodel.LugarViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GalleryFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    //Este objeto será para interactuar con el mapa de la vista...

    private lateinit var googleMap: GoogleMap
    private var mapReady = false

    //Se toman los datos de los lugares desde lugarViewModel
    private lateinit var lugarViewModel: LugarViewModel

    //Esta es una función especial que se ejecuta al crear el activity
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.map.onCreate(savedInstanceState)
        binding.map.onResume()
        binding.map.getMapAsync(this)
    }

    //Cuando el mapa está listo para mostrarse

    override fun onMapReady(map: GoogleMap) {
        map.let {
           googleMap = it
           mapReady = true
           //Se instruye al mapa para que se actualicen las ubicaciones de los lugares
           lugarViewModel.getLugares.observe(viewLifecycleOwner) { lugares ->
               updateMap(lugares)
               ubicaGPS()
           }
        }
    }

    private fun updateMap(lugares: List<Lugar>) {
        if (mapReady) {
            lugares.forEach { lugar ->
                if (lugar.latitud?.isFinite() == true &&
                        lugar.longitud?.isFinite() == true) {
                    val marca = LatLng(lugar.latitud, lugar.longitud)
                    googleMap.addMarker(MarkerOptions().position(marca).title(lugar.nombre))
                }
            }
        }
    }

    private fun ubicaGPS() {
        val fusedLocation: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION), 105)
        }
        fusedLocation.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    LatLng(9.97, -84.00/*location.latitude,location.longitude*/), 15f)
                googleMap.animateCamera(cameraUpdate)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val lugarViewModel =
            ViewModelProvider(this)[LugarViewModel::class.java]

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}