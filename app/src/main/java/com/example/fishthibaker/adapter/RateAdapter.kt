package com.example.fishthibaker.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fishthibaker.R
import com.example.fishthibaker.model.RatingModel
import com.example.fishthibaker.model.UserModel
import com.example.fishthibaker.util.Constant
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class RateAdapter(
    private val mList: ArrayList<RatingModel>,
    private val mContext: Context
) :
    RecyclerView.Adapter<RateAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvUser: TextView = itemView.findViewById(R.id.tvUser)
        val tvReview: TextView = itemView.findViewById(R.id.tvReview)
        val rbRate: RatingBar = itemView.findViewById(R.id.rbRate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_rate, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = mList[position]

        FirebaseFirestore.getInstance().collection(Constant.USER_COLLECTION).document(model.userID).get().addOnSuccessListener {
            val user = it.toObject(UserModel::class.java)
            Log.e("RateAdapter","User :: ${user?.firstName}")
            holder.tvUser.text = "${user?.firstName} ${user?.lastName}"
            holder.tvReview.text = model.review
            holder.rbRate.rating = model.rate.toFloat()
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}