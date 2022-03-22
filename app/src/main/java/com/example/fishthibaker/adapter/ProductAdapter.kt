package com.example.fishthibaker.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fishthibaker.R
import com.example.fishthibaker.model.ProductModel
import com.example.fishthibaker.ui.ProductDetailActivity

class ProductAdapter(
    private val mList: List<ProductModel>,
    private val isAdmin: Boolean,
    private val isCart: Boolean,
    private val mContext: Context
) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_product, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.tvName.text = model.name
        holder.tvCategory.text = model.category
        holder.tvDesc.text = model.description
        holder.tvPrice.text = "${model.price} Rs"
        if (isCart) {
            holder.ivCart.visibility = View.GONE
        } else {
            holder.ivCart.visibility = View.VISIBLE
        }

        if (isAdmin) {
            holder.ivCart.visibility = View.GONE
        }

        holder.ivCart.setOnClickListener {

        }

        holder.layout.setOnClickListener {
            if (!isCart) {
                val intent = Intent(mContext, ProductDetailActivity::class.java)
                intent.putExtra("product", model)
                mContext.startActivity(intent)
            }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val ivCart: ImageView = itemView.findViewById(R.id.ivCart)
        val layout: LinearLayout = itemView.findViewById(R.id.layout)
    }
}