package com.example.fishthibaker.ui

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fishthibaker.R
import com.example.fishthibaker.model.OrderModel
import com.example.fishthibaker.model.ProductModel
import com.example.fishthibaker.util.Constant
import com.google.firebase.firestore.FirebaseFirestore
import com.razorpay.Checkout
import com.razorpay.ExternalWalletListener
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject

class OrderConfirmActivity : AppCompatActivity() {

    lateinit var icProduct: ImageView

    lateinit var tvName: TextView
    lateinit var tvPhone: TextView
    lateinit var tvPrice: TextView
    lateinit var tvQuantity: TextView
    lateinit var tvAddress: TextView
    lateinit var tvTotal: TextView
    lateinit var btnBuy: Button

    var orderModel:OrderModel? = null
    var model:ProductModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirm)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Order Confirmation"

        tvName = findViewById(R.id.tvName)
        tvQuantity = findViewById(R.id.tvQuantity)
        tvAddress = findViewById(R.id.tvAddress)
        tvPrice = findViewById(R.id.tvPrice)
        tvTotal = findViewById(R.id.tvTotal)
        tvPhone = findViewById(R.id.tvPhone)
        btnBuy = findViewById(R.id.btnBuy)

        icProduct = findViewById(R.id.icProduct)
        orderModel = intent.getParcelableExtra("order") as OrderModel?
        model = intent.getSerializableExtra("product") as ProductModel?

        tvName.text = "Name : ${model?.name}"
        tvPrice.text = "Price : ${model?.weight} Rs"
        tvQuantity.text = "Quantity : ${orderModel?.quantity}"
        tvTotal.text = "TotalAmount : ${orderModel?.totalAmount} Rs"
        tvTotal.text = "Phone No. : ${orderModel?.phone}"

        tvAddress.text =
            "${orderModel?.houseNo}, ${orderModel?.society}, ${orderModel?.landmark}, ${orderModel?.city}, ${orderModel?.zip}"

        if (model!!.image.isNotEmpty()) {
            Glide.with(this).load(model!!.image)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image).into(icProduct)
        }

        btnBuy.setOnClickListener {
            val i = Intent(this,PaymentActivity::class.java)
            i.putExtra("order", orderModel)
            i.putExtra("product", model)
            startActivity(i)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}