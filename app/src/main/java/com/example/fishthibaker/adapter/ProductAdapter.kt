package com.example.fishthibaker.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fishthibaker.R
import com.example.fishthibaker.model.CartModel
import com.example.fishthibaker.model.ProductModel
import com.example.fishthibaker.ui.LoginActivity
import com.example.fishthibaker.ui.ProductDetailActivity
import com.example.fishthibaker.util.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProductAdapter(
    private val mList: List<ProductModel>,
    private val isAdmin: Boolean,
    private val isCart: Boolean,
    private val mContext: Context
) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private val mAuth = FirebaseAuth.getInstance().currentUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_product, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = mList[position]

        holder.tvName.text = model.name
        holder.tvCategory.text = model.category
        holder.tvDesc.text = model.description
        holder.tvPrice.text = "${model.price} Rs"
        if (model.image.isNotEmpty()) {
            Glide.with(mContext).load(model.image)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image).into(holder.icProductImage)
        }

        if (isCart) {
            holder.ivCart.visibility = View.VISIBLE
        } else {
            holder.ivCart.visibility = View.GONE
        }

        if (isAdmin) {
            holder.ivCart.visibility = View.GONE
        }

        holder.ivCart.setOnClickListener {
            if (mAuth != null) {
                val product =
                    FirebaseFirestore.getInstance().collection(Constant.PRODUCT_COLLECTION)
                        .document(model.id)
                val user = FirebaseFirestore.getInstance().collection(Constant.USER_COLLECTION)
                    .document(mAuth.uid)
                val cartModel = CartModel(product, user)
                FirebaseFirestore.getInstance().collection(Constant.CART_COLLECTION).add(cartModel)
            } else {
                Toast.makeText(mContext, "Guest user can't add item to cart", Toast.LENGTH_LONG)
                    .show()
                mContext.startActivity(Intent(mContext, LoginActivity::class.java))
            }
        }

        holder.layout.setOnClickListener {
            val intent = Intent(mContext, ProductDetailActivity::class.java)
            intent.putExtra("product", model)
            intent.putExtra("isCart", isCart)
            intent.putExtra("isAdmin", isAdmin)
            mContext.startActivity(intent)
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
        val icProductImage: ImageView = itemView.findViewById(R.id.icProductImage)
    }
}