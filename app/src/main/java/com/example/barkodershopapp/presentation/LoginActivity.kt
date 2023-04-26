package com.example.barkodershopapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.ActivityLoginBinding
import com.example.barkodershopapp.domain.userdataacc.UuserDataAcc
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var gso : GoogleSignInOptions
    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()



//        val  options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(this,options)
//
//        binding.singInLayout.setOnClickListener {
//            val intent = googleSignInClient.signInIntent
//            startActivityForResult(intent,10001)
//        }
//
//        binding.btnRegister.setOnClickListener {
//            var intent = Intent(this, RegisterActivity::class.java)
//            startActivity(intent)
//        }

        auth = Firebase.auth

        binding.btnLogin.setOnClickListener {
            if(performPassword()) {
                performLogin()
            }

        }
    }

    private fun performLogin() {
        var userName = binding.editTextUsername.text.toString()
        var password = binding.editTextPassword.text.toString()


        if (userName.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(userName, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Login was sucessful", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this, HomeScreenActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }


    private fun performPassword(): Boolean {
        var password = binding.editTextPassword.text
        if (password?.length!! < 8) {
            Toast.makeText(this, "Password need to be more than 8 characters", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        if (password.contains("[0-9]".toRegex())) {
                return true
        } else {
            Toast.makeText(this, "Password must contain number!", Toast.LENGTH_SHORT).show()
            return false
        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==10001){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken,null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener{task->
                    if(task.isSuccessful){
                        val databaseRef = database.reference.child("Users").child(auth.currentUser!!.uid)
                        val users = UuserDataAcc(googleSignInClient.toString(), googleSignInClient.apiKey.toString(), "**********", auth.currentUser!!.uid)
                        databaseRef.setValue(users).addOnCompleteListener {
                            if(it.isSuccessful)
                                Toast.makeText(this, "Account is sucessful created", Toast.LENGTH_SHORT)
                                    .show()
                            var intentS = Intent(this, SaveProductActivity::class.java)
                            startActivity(intentS)
                            finish()
                        }

                    }else{
                        Toast.makeText(this,task.exception?.message,Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }
//    override fun onStart() {
//        super.onStart()
//        if(FirebaseAuth.getInstance().currentUser != null){
//            val i  = Intent(this, HomeScreenActivity::class.java)
//            startActivity(i)
//        }
//    }

}



