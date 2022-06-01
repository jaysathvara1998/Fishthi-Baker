package com.example.fishthibaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fishthibaker.model.RatingModel
import com.example.fishthibaker.ui.LoginActivity
import com.example.fishthibaker.util.Constant
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RateFragment(val mContext: Context) : Fragment() {
    lateinit var rbRating: RatingBar
    lateinit var tvReview: TextInputLayout
    lateinit var etReview: TextInputEditText
    lateinit var btnSubmit: Button
    var mAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rbRating = view.findViewById(R.id.rbRating)
        tvReview = view.findViewById(R.id.tvReview)
        etReview = view.findViewById(R.id.etReview)
        btnSubmit = view.findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            if (mAuth.currentUser != null) {
                val rate = rbRating.rating.toString()
                val review = etReview.text.toString()
                val userID = mAuth.currentUser!!.uid

                val model = RatingModel(rate, review, userID)

                db.collection(Constant.RATING_COLLECTION).add(model).addOnSuccessListener {
                    Toast.makeText(mContext, "Review submitted successfully", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(mContext, "Guest user can't add review", Toast.LENGTH_LONG).show()
                startActivity(Intent(mContext, LoginActivity::class.java))
            }
        }
    }
}