package com.example.fishthibaker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fishthibaker.model.UserModel
import com.example.fishthibaker.util.Constant
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap




class ProfileFragment(val mContext: Context) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvFName: TextInputLayout = view.findViewById(R.id.tvFName)
        val tvLName: TextInputLayout = view.findViewById(R.id.tvLName)
        val tvAddress: TextInputLayout = view.findViewById(R.id.tvAddress)
        val tvEmail: TextInputLayout = view.findViewById(R.id.tvEmail)
        val tvPhone: TextInputLayout = view.findViewById(R.id.tvPhone)

        val btnUpdate: Button = view.findViewById(R.id.btnUpdate)

        val etFName: TextInputEditText = view.findViewById(R.id.etFName)
        val etLName: TextInputEditText = view.findViewById(R.id.etLName)
        val etAddress: TextInputEditText = view.findViewById(R.id.etAddress)
        val etEmail: TextInputEditText = view.findViewById(R.id.etEmail)
        val etPhone: TextInputEditText = view.findViewById(R.id.etPhone)

        val mAuth = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        db.collection(Constant.USER_COLLECTION).document(mAuth!!.uid).get().addOnSuccessListener {
            val model: UserModel? = it.toObject(UserModel::class.java)
            etFName.setText(model!!.firstName)
            etLName.setText(model.lastName)
            etAddress.setText(model.address)
            etEmail.setText(model.email)
            etPhone.setText(model.phone)

        }

        btnUpdate.setOnClickListener {
            val email = etEmail.text.toString()
            val firstName = etFName.text.toString()
            val lastName = etLName.text.toString()
            val phone = etPhone.text.toString()
            val address = etAddress.text.toString()

            val data: MutableMap<String, Any> = HashMap()
            data["email"] = email
            data["address"] = address
            data["phone"] = phone
            data["firstName"] = firstName
            data["lastName"] = lastName

            db.collection(Constant.USER_COLLECTION).document(mAuth.uid).update(data).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(mContext,"Profile update successfully",Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(mContext,it.message,Toast.LENGTH_SHORT).show()
            }
        }
    }
}