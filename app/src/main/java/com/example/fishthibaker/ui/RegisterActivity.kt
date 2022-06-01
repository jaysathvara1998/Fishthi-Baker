package com.example.fishthibaker.ui

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fishthibaker.R
import com.example.fishthibaker.model.UserModel
import com.example.fishthibaker.util.Constant
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    var etFName: TextInputEditText? =
        null
    var etLName: TextInputEditText? =
        null
    var etEmail: TextInputEditText? = null
    var etPassword: TextInputEditText? = null
    var etPhone: TextInputEditText? = null
    var etAddress: TextInputEditText? = null

    var tvFName: TextInputLayout? = null
    var tvLName: TextInputLayout? = null
    var tvAddress: TextInputLayout? = null

    var tvEmail: TextInputLayout? = null
    var tvPhone: TextInputLayout? = null
    var tvPassword: TextInputLayout? = null

    var firstName = ""
    var lastName = ""
    var email: String? = ""
    var password: String? = ""
    var phone: String? = ""
    var address: String? = ""

    var btnRegister: Button? = null

    var ibBack: ImageButton? = null

    var items = arrayOf(
        "Service Provider",
        "Service Seeker"
    )
    var userTypeList = arrayOf(
        "1",
        "2"
    )
    var adapter: ArrayAdapter<String>? = null
    var categoryAdapter: ArrayAdapter<String>? = null
    var categoryList = arrayOf(
        "AC Repair",
        "Car Cleaning",
        "Carpenter",
        "Painter",
        "Photographer",
        "Electrician",
        "Computer Service",
        "Salon",
        "Cleaning Service",
        "Plumbers"
    )
    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    private var mAuth: FirebaseAuth? = null
//    var db = FirebaseDatabase.getInstance()
    val db = FirebaseFirestore.getInstance()

    private var dialog: ProgressDialog? = null
    var builder: AlertDialog.Builder? = null
    var userModel: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()
        listener()
    }

    private fun init() {
        etFName = findViewById(R.id.etFName)
        etLName = findViewById(R.id.etLName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etPhone = findViewById(R.id.etContact)

        btnRegister = findViewById(R.id.btnRegister)
        etAddress = findViewById(R.id.etAddress)
        tvFName = findViewById(R.id.tvFName)
        tvLName = findViewById(R.id.tvLName)
        tvAddress = findViewById(R.id.tvHouseNumber)
        tvEmail = findViewById(R.id.tvEmail)
        tvPhone = findViewById(R.id.tvContact)
        tvPassword = findViewById(R.id.tvPassword)

        mAuth = FirebaseAuth.getInstance()
        dialog = ProgressDialog(this)
        dialog!!.setMessage("Please Wait....!")

        adapter = ArrayAdapter(applicationContext, R.layout.list_item_text, items)
        builder = AlertDialog.Builder(this)
        builder!!.setTitle(R.string.app_name).setCancelable(false)
    }

    private fun listener() {

        btnRegister!!.setOnClickListener { v: View? ->
            email = etEmail!!.text.toString()
            firstName = etFName!!.text.toString()
            lastName = etLName!!.text.toString()
            password = etPassword!!.text.toString()
            phone = etPhone!!.text.toString()
            address = etAddress!!.text.toString()
            if (validate()) {
                dialog!!.show()
                mAuth!!.createUserWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                "Registration successful!",
                                Toast.LENGTH_LONG
                            ).show()
                            task.addOnSuccessListener { authResult ->
                                if (!Objects.requireNonNull(authResult.user)?.isEmailVerified!!
                                ) authResult.user!!.sendEmailVerification()
                                val id = authResult.user!!.uid
                                userModel = UserModel(
                                    firstName,
                                    lastName,
                                    id,
                                    email,
                                    password,
                                    address,
                                    phone,
                                    "",
                                    false
                                )
                                db.collection(Constant.USER_COLLECTION).document(id).set(userModel!!)
//                                db.getReference(Constant.USER_COLLECTION).child(id)
//                                    .setValue(userModel)

                                dialog!!.dismiss()
                                mAuth!!.signOut()
                                val intent =
                                    Intent(
                                        applicationContext,
                                        LoginActivity::class.java
                                    )
                                startActivity(intent)
                            }
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Registration failed! Please try again later",
                                Toast.LENGTH_LONG
                            ).show()
                            dialog!!.dismiss()
                        }
                        task.addOnFailureListener { e ->
                            Log.e("RegisterActivity", "OnFail : " + e.message)
                            builder!!.setMessage(e.message).setPositiveButton(
                                "OK"
                            ) { dialog, which -> dialog.dismiss() }
                            builder!!.show()
                            e.printStackTrace()
                        }
                    }
            }
        }
    }

    private fun validate(): Boolean {
        if (etFName!!.text.toString().isEmpty()) {
            tvFName!!.error = "Enter First Name"
            etFName!!.isFocusable = true
            return false
        } else {
            tvFName!!.error = ""
        }
        if (etLName!!.text.toString().isEmpty()) {
            tvLName!!.error = "Enter Last Name"
            etLName!!.isFocusable = true
            return false
        } else {
            tvLName!!.error = ""
        }
        if (address!!.isEmpty()) {
            tvAddress!!.error = "Enter Address"
            etAddress!!.isFocusable = true
            return false
        } else {
            tvAddress!!.error = ""
        }

        if (!email!!.matches(Regex(emailPattern))) {
            tvEmail!!.error = "Enter valid email"
            etEmail!!.isFocusable = true
            return false
        } else {
            tvEmail!!.error = ""
        }
        if (phone!!.isEmpty()) {
            tvPhone!!.error = "Enter phone number"
            etPhone!!.isFocusable = true
            return false
        } else {
            tvPhone!!.error = ""
        }
        if (!isValidMobile(phone!!)) {
            tvPhone!!.error = "Enter valid mobile number"
            etPhone!!.isFocusable = true
            return false
        } else {
            tvPhone!!.error = ""
        }
        if (etPassword!!.text.toString().isEmpty()) {
            tvPassword!!.error = "Enter password"
            etPassword!!.isFocusable = true
            return false
        }
        if (etPassword!!.text.toString().length < 8) {
            tvPassword!!.error =
                "Password should be 8 character long"
            return false
        } else if (!isValidPassword(etPassword!!.text.toString())) {
            tvPassword!!.error =
                "Password must contain Number, Lowercase, Uppercase and Special symbol"
            return false
        } else {
            tvPassword!!.error = ""
        }
        return true
    }

    fun isValidPassword(password: String?): Boolean {
        val PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=])(?=\\\\S+\$).{4,}\$"
        val pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }

    private fun isValidMobile(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }
}