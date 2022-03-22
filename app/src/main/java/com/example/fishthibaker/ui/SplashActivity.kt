package com.example.fishthibaker.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fishthibaker.R
import com.example.fishthibaker.model.UserModel
import com.example.fishthibaker.util.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

class SplashActivity : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        pref = getSharedPreferences(Constant.PREF_NAME, MODE_PRIVATE)
        mAuth = FirebaseAuth.getInstance()

        val gson = Gson()

        Log.e("SplashActivity","Model :: ${mAuth!!.currentUser != null}")
        val model: UserModel = if (mAuth!!.currentUser != null) {
            gson.fromJson(
                pref!!.getString(Constant.USER_COLLECTION, ""),
                UserModel::class.java
            )
        } else {
            UserModel()
        }

        Handler().postDelayed({
            if (mAuth!!.currentUser == null) {
                startActivity(
                    Intent(
                        applicationContext,
                        LoginActivity::class.java
                    )
                )
            } else {
                startActivity(
                    Intent(
                        applicationContext,
                        UserHomeActivity::class.java
                    )
                )
            }
            finish()
        }, 1000)
    }
}