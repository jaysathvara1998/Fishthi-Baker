package com.example.fishthibaker

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.fishthibaker.model.UserModel
import com.example.fishthibaker.util.Constant
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class ProfileFragment(val mContext: Context) : Fragment() {

    lateinit var icProfile: CircleImageView
    var isImageSelect: Boolean = false
    var uri: Uri? = null
    private var dialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog = ProgressDialog(mContext)
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvFName: TextInputLayout = view.findViewById(R.id.tvFName)
        val tvLName: TextInputLayout = view.findViewById(R.id.tvLName)
        val tvAddress: TextInputLayout = view.findViewById(R.id.tvHouseNumber)
        val tvEmail: TextInputLayout = view.findViewById(R.id.tvEmail)
        val tvPhone: TextInputLayout = view.findViewById(R.id.tvPhone)
        icProfile = view.findViewById(R.id.icProfile)

        val btnUpdate: Button = view.findViewById(R.id.btnUpdate)

        val etFName: TextInputEditText = view.findViewById(R.id.etFName)
        val etLName: TextInputEditText = view.findViewById(R.id.etLName)
        val etAddress: TextInputEditText = view.findViewById(R.id.etAddress)
        val etEmail: TextInputEditText = view.findViewById(R.id.etEmail)
        val etPhone: TextInputEditText = view.findViewById(R.id.etPhone)

        icProfile.setOnClickListener {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        val mAuth = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        db.collection(Constant.USER_COLLECTION).document(mAuth!!.uid).get().addOnSuccessListener {
            val model: UserModel? = it.toObject(UserModel::class.java)
            etFName.setText(model!!.firstName)
            etLName.setText(model.lastName)
            etAddress.setText(model.address)
            etEmail.setText(model.email)
            etPhone.setText(model.phone)

            if (model.profileImage != null) {
                Glide.with(mContext).load(model.profileImage!!)
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .error(R.drawable.ic_profile_placeholder).into(icProfile)
            }

        }

        btnUpdate.setOnClickListener {
            val email = etEmail.text.toString()
            val firstName = etFName.text.toString()
            val lastName = etLName.text.toString()
            val phone = etPhone.text.toString()
            val address = etAddress.text.toString()

            dialog!!.setMessage("Updating user profile")
            dialog!!.show()

            val data: MutableMap<String, Any> = HashMap()
            data["email"] = email
            data["address"] = address
            data["phone"] = phone
            data["firstName"] = firstName
            data["lastName"] = lastName

            if (isImageSelect) {
                val storage = FirebaseStorage.getInstance().reference
                val mStorage = storage.child("image/$email.png")
                mStorage.putFile(uri!!).addOnSuccessListener { task ->
                    task.storage.downloadUrl.addOnSuccessListener {
                        updateDataToFirebase(db, mAuth, data, it.toString())
                    }
                }
            } else {
                updateDataToFirebase(db, mAuth, data, null)
            }

        }
    }

    private fun updateDataToFirebase(
        db: FirebaseFirestore,
        mAuth: FirebaseUser,
        data: MutableMap<String, Any>,
        uri: String?
    ) {
        if (uri != null) {
            data["profileImage"] = uri
        }
        db.collection(Constant.USER_COLLECTION).document(mAuth.uid).update(data)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(mContext, "Profile update successfully", Toast.LENGTH_SHORT)
                        .show()
                }
                if (dialog!!.isShowing) {
                    dialog!!.dismiss();
                }
            }.addOnFailureListener {
                Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()
                if (dialog!!.isShowing) {
                    dialog!!.dismiss();
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                uri = data?.data!!
                icProfile.setImageURI(uri)
                isImageSelect = true
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(mContext, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(mContext, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}