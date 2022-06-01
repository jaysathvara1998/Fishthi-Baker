package com.example.fishthibaker.ui

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fishthibaker.R
import com.example.fishthibaker.model.UserModel
import com.example.fishthibaker.util.Constant
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {
    lateinit var btnLogin: Button
    lateinit var btnRegister: Button

    lateinit var etEmail: TextInputEditText
    lateinit var etPassword: TextInputEditText
    lateinit var tvEmail: TextInputLayout
    lateinit var tvPassword: TextInputLayout

    private var mAuth: FirebaseAuth? = null

    var db = FirebaseFirestore.getInstance()
    var tvSignUp: TextView? = null
    var tvForgotPass: TextView? = null
    var tvSkip: TextView? = null

    var email: String? = null
    var password: kotlin.String? = null
    var sharedpreferences: SharedPreferences? = null

    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private var dialog: ProgressDialog? = null
    var builder: AlertDialog.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initialize()
        listener()
    }

    private fun initialize() {
        sharedpreferences = getSharedPreferences(Constant.PREF_NAME, MODE_PRIVATE)

        dialog = ProgressDialog(this)
        dialog!!.setMessage("Please Wait....!")
        dialog!!.setCancelable(false)

        mAuth = FirebaseAuth.getInstance()
        tvForgotPass = findViewById(R.id.tvForgotPass)
        tvSkip = findViewById(R.id.tvSkip)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        tvPassword = findViewById(R.id.tvPassword)
        tvEmail = findViewById(R.id.tvEmail)

        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)
    }

    private fun listener() {
        btnLogin.setOnClickListener {
            email = etEmail.text.toString()
            password = etPassword.text.toString()

            if (email == "admin" && password == "admin") {
                val intent = Intent(
                    applicationContext,
                    AdminHomeActivity::class.java
                )
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else if (validate()) {
                dialog!!.show()
                mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                    .addOnSuccessListener { authResult ->
                        Log.e("LoginActivity", "AuthResult : " + authResult.user!!.uid)
                        db.collection(Constant.USER_COLLECTION).document(authResult.user!!.uid).get().addOnCompleteListener { task ->
                            dialog!!.dismiss()
                            if (task.isSuccessful) {
                                val user: UserModel? = task.result!!.toObject(UserModel::class.java)
                                val gson = Gson()
                                val json: String = gson.toJson(user)
                                val editor = sharedpreferences!!.edit()
                                editor.putString(Constant.USER_COLLECTION, json)
                                editor.apply()

                                val intent = Intent(
                                    applicationContext,
                                    UserHomeActivity::class.java
                                )
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)

                            } else {
                                task.exception!!.printStackTrace()
                            }
                        }

                    }.addOnFailureListener { e ->
                        dialog!!.dismiss()
                        Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
            }
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        tvSkip!!.setOnClickListener {
            startActivity(Intent(this, UserHomeActivity::class.java))
        }

        tvForgotPass!!.setOnClickListener {
            startActivity(Intent(this, ForgotActivity::class.java))
        }
    }

    private fun validate(): Boolean {
        if (!email!!.matches(Regex(emailPattern))) {
            etEmail.error = "Enter valid email"
            etEmail.isFocusable = true
            return false
        }
        if (etPassword.text.toString().isEmpty()) {
            etPassword.error = "Enter password"
            etPassword.isFocusable = true
            return false
        }
        return true
    }
}