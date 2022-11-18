package com.example.lugares_v

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.lugares_v.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient

    //Definimos un objeto para acceder a la autenticación de Firebase
    private lateinit var auth : FirebaseAuth

    //Definimos un objeto para acceder a los elementos de la pantalla xml
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicializar la autenticación
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        //Definir el evento onclick del botón Register
        binding.btRegister.setOnClickListener { haceRegistro() }

        //Definir el evento onclick del botón Login
        binding.btLogin.setOnClickListener { haceLogin() }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.defaul_web_client_id_r))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)
        binding.btgoogle.setOnClickListener{googleSignIn()}

    }

    private fun googleSignIn(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent,5000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 5000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val cuenta = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(cuenta.idToken!!)
            } catch (e: ApiException) {

            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    refresca(user)
                } else {
                    refresca(null)
                }
            }
    }

    private fun haceRegistro() {
        //recupero la información que el usuario escribió
        val email = binding.etCorreo.text.toString()
        val clave = binding.etClave.text.toString()

        //Utilizo el objeto auth para hacer el registro...
        auth.createUserWithEmailAndPassword(email, clave)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful){
                    val user = auth.currentUser
                    refresca(user)
                } else {
                    Toast.makeText(baseContext,"Falló",Toast.LENGTH_LONG).show()
                    refresca(null)
                }
            }
    }

    private fun refresca(user: FirebaseUser?) {
        if(user != null){
            val intent = Intent(this, Principal::class.java)
            startActivity(intent)
        }
    }

    private fun haceLogin() {
        //recupero la información que el usuario escribió
        val email = binding.etCorreo.text.toString()
        val clave = binding.etClave.text.toString()

        //Utilizo el objeto auth para hacer el registro...
        auth.signInWithEmailAndPassword(email, clave)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful){
                    val user = auth.currentUser
                    refresca(user)
                } else {
                    Toast.makeText(baseContext,"Falló",Toast.LENGTH_LONG).show()
                    refresca(null)
                }
            }
    }

    //Esto se ejecuta toda vez que se presente el app en la pantallas, valida si hay un usuario autenticado
    public override fun onStart() {
        super.onStart()
        val usuario = auth.currentUser
    }
}