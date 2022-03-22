package com.example.fishthibaker.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fishthibaker.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class ForgotActivity : AppCompatActivity() {
    var tvEmail: TextInputLayout? = null
    var etEmail: TextInputEditText? = null
    var btnForgot: Button? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        mAuth = FirebaseAuth.getInstance()

        tvEmail = findViewById(R.id.tvEmail)
        etEmail = findViewById(R.id.etEmail)
        btnForgot = findViewById(R.id.btnForgot)

        btnForgot!!.setOnClickListener {
            if (etEmail!!.text.toString().isEmpty()) {
                tvEmail!!.error = "Please enter Email"
            } else {
                tvEmail!!.error = ""
                mAuth!!.sendPasswordResetEmail(etEmail!!.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Email sent successfully", Toast.LENGTH_LONG).show()
                        onBackPressed()
                    } else {
                        it.exception!!.printStackTrace()
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}