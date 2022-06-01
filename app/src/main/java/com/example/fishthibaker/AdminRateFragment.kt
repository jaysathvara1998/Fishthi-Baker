package com.example.fishthibaker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fishthibaker.adapter.RateAdapter
import com.example.fishthibaker.model.RatingModel
import com.example.fishthibaker.util.Constant
import com.google.firebase.firestore.FirebaseFirestore

class AdminRateFragment(var mContext: Context) : Fragment() {
    lateinit var rvRate: RecyclerView
    val db = FirebaseFirestore.getInstance()
    val rateList: ArrayList<RatingModel> = ArrayList()
    lateinit var adapter :RateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_rate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvRate = view.findViewById(R.id.rvRate)

        adapter = RateAdapter(rateList,mContext)

        rvRate.layoutManager = LinearLayoutManager(mContext)
        rvRate.adapter = adapter

        db.collection(Constant.RATING_COLLECTION).get().addOnSuccessListener {
            for (item in it.documents) {
                val rate = item["rate"] as String
                val review = item["review"] as String
                val userID = item["userID"] as String

                val model = RatingModel(rate, review, userID)
                rateList.add(model)
                adapter.notifyDataSetChanged()
            }
        }
    }
}