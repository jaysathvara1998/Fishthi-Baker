package com.example.fishthibaker.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fishthibaker.R
import com.example.fishthibaker.model.CartModel
import com.example.fishthibaker.model.ProductModel
import com.example.fishthibaker.util.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class ProductDetailActivity : AppCompatActivity() {
    var model: ProductModel? = null
    var isCart: Boolean = true
    var isAdmin: Boolean = false
    lateinit var tvName: TextView
    lateinit var tvDesc: TextView
    lateinit var tvCategory: TextView
    lateinit var tvPrice: TextView
    lateinit var tvWeight: TextView
    lateinit var btnCart: Button
    lateinit var btnBuy: Button
    lateinit var btnEdit: Button
    lateinit var btnDelete: Button
    lateinit var icProduct: ImageView
    lateinit var layoutButton: LinearLayout
    lateinit var layoutUpdateButton: LinearLayout
    val mAuth = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        model = intent.getSerializableExtra("product") as ProductModel?
        isCart = intent.getBooleanExtra("isCart", false)
        isAdmin = intent.getBooleanExtra("isAdmin", false)
        var userRef: DocumentReference? = null

        if (FirebaseAuth.getInstance().currentUser != null){
            userRef = FirebaseFirestore.getInstance().collection(Constant.USER_COLLECTION)
                .document(FirebaseAuth.getInstance().currentUser!!.uid)
        }

        val productRef = FirebaseFirestore.getInstance().collection(Constant.PRODUCT_COLLECTION)
            .document(model!!.id)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = model!!.name

        tvName = findViewById(R.id.tvName)
        tvDesc = findViewById(R.id.tvDesc)
        tvCategory = findViewById(R.id.tvCategory)
        tvPrice = findViewById(R.id.tvPrice)
        tvWeight = findViewById(R.id.tvWeight)
        btnCart = findViewById(R.id.btnCart)
        btnBuy = findViewById(R.id.btnBuy)
        btnEdit = findViewById(R.id.btnEdit)
        btnDelete = findViewById(R.id.btnDelete)
        icProduct = findViewById(R.id.icProduct)
        layoutButton = findViewById(R.id.layoutButton)
        layoutUpdateButton = findViewById(R.id.layoutUpdateButton)

        db.collection(Constant.CART_COLLECTION).whereEqualTo("userRef", userRef)
            .whereEqualTo("productRef", productRef).get().addOnSuccessListener {
                for (item in it.documents) {
                    btnCart.text = "Remove from cart"
                }
            }

        if (isAdmin) {
            layoutUpdateButton.visibility = View.VISIBLE
        } else {
            layoutUpdateButton.visibility = View.GONE
        }

        tvName.text = model!!.name
        tvDesc.text = model!!.description
        tvCategory.text = model!!.category
        tvPrice.text = model!!.price + " Rs"
        tvWeight.text = model!!.weight
        if (model!!.image.isNotEmpty()) {
            Glide.with(this).load(model!!.image)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image).into(icProduct)
        }

        btnCart.setOnClickListener {
            if (mAuth != null) {
                if (btnCart.text == "Remove from cart") {
                    db.collection(Constant.CART_COLLECTION).whereEqualTo("userRef", userRef)
                        .whereEqualTo("productRef", productRef).get().addOnSuccessListener {
                            for(item in it.documents){
                                item.reference.delete().addOnSuccessListener {
                                    Toast.makeText(this,"Remove",Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                } else {
                    val product =
                        FirebaseFirestore.getInstance().collection(Constant.PRODUCT_COLLECTION)
                            .document(model!!.id)
                    val user = FirebaseFirestore.getInstance().collection(Constant.USER_COLLECTION)
                        .document(mAuth.uid)
                    val cartModel = CartModel(product, user)
                    FirebaseFirestore.getInstance().collection(Constant.CART_COLLECTION)
                        .add(cartModel)
                }
            } else {
                Toast.makeText(this, "Guest user can't add item to cart", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        btnBuy.setOnClickListener {
            if (mAuth != null) {
                val i = Intent(this, CheckoutActivity::class.java)
                i.putExtra("product", model)
                startActivity(i)
            } else {
                Toast.makeText(this, "Guest user can't buy item", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        btnEdit.setOnClickListener {
            val i = Intent(this, AddProductActivity::class.java)
            i.putExtra("isEdit", true)
            i.putExtra("model", model)
            startActivity(i)
        }

        btnDelete.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            try {
                Log.e("ProductDetail", "Model :: ${model}")
                db.collection(Constant.PRODUCT_COLLECTION).document(model!!.id).delete()
                    .addOnSuccessListener {
                        onBackPressed()
                    }.addOnFailureListener {
                        it.printStackTrace()
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}