package com.example.fishthibaker.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fishthibaker.R
import com.example.fishthibaker.model.CartModel
import com.example.fishthibaker.model.ProductModel
import com.example.fishthibaker.util.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProductDetailActivity : AppCompatActivity() {
    var model: ProductModel? = null
    lateinit var tvName: TextView
    lateinit var tvDesc: TextView
    lateinit var tvCategory: TextView
    lateinit var tvPrice: TextView
    lateinit var tvWeight: TextView
    lateinit var btnCart: Button
    lateinit var btnBuy: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        model = intent.getSerializableExtra("product") as ProductModel?

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        tvName = findViewById(R.id.tvName)
        tvDesc = findViewById(R.id.tvDesc)
        tvCategory = findViewById(R.id.tvCategory)
        tvPrice = findViewById(R.id.tvPrice)
        tvWeight = findViewById(R.id.tvWeight)
        btnCart = findViewById(R.id.btnCart)
        btnBuy = findViewById(R.id.btnBuy)

        tvName.text = model!!.name
        tvDesc.text = model!!.description
        tvCategory.text = model!!.category
        tvPrice.text = model!!.price + " Rs"
        tvWeight.text = model!!.weight

        btnCart.setOnClickListener {
            val mAuth = FirebaseAuth.getInstance().currentUser
            val product = FirebaseFirestore.getInstance().collection(Constant.PRODUCT_COLLECTION)
                .document(model!!.id)
            val user = FirebaseFirestore.getInstance().collection(Constant.USER_COLLECTION)
                .document(mAuth!!.uid)
            val cartModel = CartModel(product, user)
            FirebaseFirestore.getInstance().collection(Constant.CART_COLLECTION).add(cartModel)
        }

        btnBuy.setOnClickListener {

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}