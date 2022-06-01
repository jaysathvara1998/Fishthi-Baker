package com.example.fishthibaker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.fishthibaker.util.Constant
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore

class AdminTCFragment(var mContext: Context) : Fragment() {

    val db = FirebaseFirestore.getInstance()

    var tvTC: TextInputLayout? = null
    var etTC: TextInputEditText? = null

    var btnSubmit: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_t_c, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTC = view.findViewById(R.id.tvTC)
        etTC = view.findViewById(R.id.etTC)

        db.collection(Constant.TC_COLLECTION).document("tc").get().addOnSuccessListener {
            if(it.contains("tc")){
                val item = it.data
                val tc = item?.get("tc") as String

                etTC!!.setText(tc)
            }
        }

        btnSubmit = view.findViewById(R.id.btnSubmit)

        btnSubmit!!.setOnClickListener {
            if (etTC!!.text.toString().isNotEmpty()) {
                val data = HashMap<String, String>()
                data["tc"] = etTC!!.text.toString()

                db.collection(Constant.TC_COLLECTION).document("tc").set(data)
            }
        }
    }
}