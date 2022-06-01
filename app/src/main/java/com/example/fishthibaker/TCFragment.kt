package com.example.fishthibaker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fishthibaker.util.Constant
import com.google.firebase.firestore.FirebaseFirestore

class TCFragment : Fragment() {

    lateinit var tvTC: TextView
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_t_c, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTC = view.findViewById(R.id.tvTC)

        db.collection(Constant.TC_COLLECTION).get().addOnSuccessListener {
            for (item in it.documents) {
                val tc = item["tc"] as String
                tvTC.text = tc
            }
        }
    }
}