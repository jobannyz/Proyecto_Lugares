package com.example.lugares_v.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lugares_v.model.Lugar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase

class LugarDao {

    //Valores para la estructura de FireStore Cloud
    private val coleccion1 = "lugaresApp"
    private val usuario = Firebase.auth.currentUser?.email.toString()
    private val coleccion2 = "misLugares"

    //Objeto para la "conexión" de la base de datos en la nube
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    //Se recibe un objeto lugar, se valida si el id tiene algo... es un aactualización, sino se crea
    fun saveLugar(lugar: Lugar) {
        val documento: DocumentReference
        if (lugar.id.isEmpty()) {
            documento = firestore
                .collection(coleccion1)
                .document(usuario)
                .collection(coleccion2)
                .document()
            lugar.id = documento.id
        } else {
            documento = firestore
                .collection(coleccion1)
                .document(usuario)
                .collection(coleccion2)
                .document(lugar.id)
        }
        //ahora si se va a registrar la info (nueva o se actualiza)

        //"registra" la actualización
        documento.set(lugar)
            .addOnSuccessListener {
                Log.d("saveLugar", "Lugar agregado/actualizado")
            }
            .addOnCanceledListener {
                Log.e("saveLugar", "Lugar NO agregado/actualizado")
            }

    }
    fun deleteLugar(lugar: Lugar) {
        if (lugar.id.isNotEmpty()) {
            firestore
                .collection(coleccion1)
                .document(usuario)
                .collection(coleccion2)
                .document(lugar.id)
                .delete()
                .addOnSuccessListener {
                    Log.d("deleteLugar", "Lugar eliminado")
                }
                .addOnCanceledListener {
                    Log.e("deleteLugar", "Lugar NO eliminado")
                }
        }
    }
    fun getLugares() : MutableLiveData<List<Lugar>> {
        val listaLugares = MutableLiveData<List<Lugar>>()

        firestore
            .collection(coleccion1)
            .document(usuario)
            .collection(coleccion2)
            .addSnapshotListener { // Se materializó algún error en la generación de la instantánea
                instantanea, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                //Si estamos en esta línea... entonces si se tomó la instantánea
                if (instantanea != null) {
                    val lista = ArrayList<Lugar>()

                    //Se recorre la instantánea para transformar cada documento en un objeto lugar
                    instantanea.documents.forEach {
                        val lugar = it.toObject(Lugar::class.java)
                        if (lugar != null) { //Si se transformó en objeto lugar... el documento
                            lista.add(lugar) //Se agrega el lugar a la lista...
                        }
                    }
                    listaLugares.value = lista
                }
            }

        return listaLugares
    }
}