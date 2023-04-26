package com.example.barkodershopapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.barkoder.shoppingApp.net.databinding.ActivityRegisterBinding
import com.example.barkodershopapp.domain.userdataacc.UuserDataAcc
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackActivityR.setOnClickListener {
            finish()
        }


        database = FirebaseDatabase.getInstance()
        auth = Firebase.auth

        binding.btnRegisterRegister.setOnClickListener {
                performSingup()



        }
    }


        fun performSingup() {
            var name = binding.editTextRegisterName.text.toString()
            var email = binding.editTextRegisterUsername.text.toString()
            var password = binding.editTextRegisterPassword.text.toString()
            var confirmedPassword = binding.editTextRegisterConfirmedPassword.text.toString()

            if(name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmedPassword.isNotEmpty()) {

                if(confirmedPassword == password) {
                    if (performPassword()) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->

                                if (task.isSuccessful) {
                                    val databaseRef = database.reference.child("Users")
                                        .child(auth.currentUser!!.uid)
                                    val users =
                                        UuserDataAcc(name, email, password, auth.currentUser!!.uid)
                                    databaseRef.setValue(users).addOnCompleteListener {
                                        if (it.isSuccessful)
                                            Toast.makeText(
                                                applicationContext,
                                                "Account is sucessful created",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        var intentS =
                                            Intent(this@RegisterActivity, LoginActivity::class.java)
                                        startActivity(intentS)
                                        finish()
                                    }

                                }

                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    "Error occured ${it.localizedMessage}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
                else {
                    Toast.makeText(this, "Confirmed password is not correct", Toast.LENGTH_SHORT).show()
                }
            }

            else {
                Toast.makeText(this, "Fileds cant be empty", Toast.LENGTH_SHORT).show()
            }



        }

    private fun performPassword(): Boolean {
        var password = binding.editTextRegisterPassword.text
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


    }
