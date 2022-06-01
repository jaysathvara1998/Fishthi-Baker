package com.example.fishthibaker.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fishthibaker.R
import com.example.fishthibaker.model.OrderModel
import com.example.fishthibaker.model.ProductModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth

class CheckoutActivity : AppCompatActivity() {
    lateinit var tvName: TextView
    lateinit var tvDesc: TextView
    lateinit var tvPrice: TextView
    lateinit var tvWeight: TextView
    lateinit var icProduct: ImageView
    lateinit var btnBuy: Button
    lateinit var tvTotal: TextView

    lateinit var tvHouseNumber: TextInputLayout
    lateinit var etHouseNumber: TextInputEditText

    lateinit var tvSociety: TextInputLayout
    lateinit var etSociety: TextInputEditText

    lateinit var tvLandmark: TextInputLayout
    lateinit var etLandmark: TextInputEditText

    lateinit var tvCity: TextInputLayout
    lateinit var etCity: TextInputEditText

    lateinit var tvState: TextInputLayout
    lateinit var etState: TextInputEditText

    lateinit var tvPhone: TextInputLayout
    lateinit var etPhone: TextInputEditText

    lateinit var tvZip: TextInputLayout
    lateinit var etZip: TextInputEditText

    lateinit var tvQuantity: TextInputLayout
    lateinit var etQuantity: TextInputEditText

    private lateinit var tvWeight1: TextInputLayout
    private lateinit var etWeight: TextInputEditText

    lateinit var tvDesc1: TextInputLayout
    lateinit var etDesc: TextInputEditText

    var model: ProductModel? = null
    var mAuth = FirebaseAuth.getInstance().currentUser
    var quantity = 0
    var price = 0
    var totalPrice = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Checkout"

        model = intent.getSerializableExtra("product") as ProductModel?

        tvName = findViewById(R.id.tvName)
        tvDesc = findViewById(R.id.tvDesc)
        tvPrice = findViewById(R.id.tvPrice)
        tvWeight = findViewById(R.id.tvWeight)
        tvHouseNumber = findViewById(R.id.tvHouseNumber)
        etHouseNumber = findViewById(R.id.etHouseNumber)

        tvSociety = findViewById(R.id.tvSociety)
        etSociety = findViewById(R.id.etSociety)

        tvLandmark = findViewById(R.id.tvLandmark)
        etLandmark = findViewById(R.id.etLandmark)

        tvCity = findViewById(R.id.tvCity)
        etCity = findViewById(R.id.etCity)

        tvState = findViewById(R.id.tvState)
        etState = findViewById(R.id.etState)

        tvPhone = findViewById(R.id.tvPhone)
        etPhone = findViewById(R.id.etPhone)

        tvZip = findViewById(R.id.tvZip)
        etZip = findViewById(R.id.etZip)

        icProduct = findViewById(R.id.icProduct)
        btnBuy = findViewById(R.id.btnBuy)
        tvTotal = findViewById(R.id.tvTotal)
        etQuantity = findViewById(R.id.etQuantity)
        etWeight = findViewById(R.id.etWeight)
        tvWeight1 = findViewById(R.id.tvWeight1)
        tvDesc1 = findViewById(R.id.tvDesc1)
        etDesc = findViewById(R.id.etDesc)

        tvName.text = model!!.name
        tvDesc.text = model!!.description
        tvWeight.text = model!!.weight
        tvPrice.text = model!!.price + " Rs"
        setTotalAmount()
        try {
            if (model!!.price.isNotEmpty()) {
                price = model!!.price.toInt()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        etQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                quantity = 0
                if (s.toString().isNotEmpty()) {
                    quantity = s.toString().toInt()
                }
                Log.e("CheckoutActivity", "Quantity :: $s Quantity1 :: $quantity")

                totalPrice = quantity * price
                setTotalAmount()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        if (model!!.image.isNotEmpty()) {
            Glide.with(this).load(model!!.image)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image).into(icProduct)
        }

        btnBuy.setOnClickListener {
            if (validate()) {
                val orderModel: OrderModel =
                    OrderModel(
                        "",
                        etHouseNumber.text.toString(),
                        etSociety.text.toString(),
                        etLandmark.text.toString(),
                        etCity.text.toString(),
                        etState.text.toString(),
                        etPhone.text.toString(),
                        etZip.text.toString(),
                        model!!.id,
                        mAuth!!.uid,
                        Timestamp.now(),
                        quantity,
                        totalPrice,
                        "In-Progress",
                        etDesc.text.toString()
                    )

                val i = Intent(this, OrderConfirmActivity::class.java)
                i.putExtra("order", orderModel)
                i.putExtra("product", model)
                startActivity(i)
            }
        }
    }

    fun validate(): Boolean {
        /*if (etWeight.text!!.isEmpty() || etWeight.text!!.toString() == "0") {
            Toast.makeText(this, "Enter valid weight", Toast.LENGTH_LONG).show()
            return true
        }*/
        if (etQuantity.text!!.isEmpty() || etWeight.text!!.toString() == "0") {
            Toast.makeText(this, "Enter valid quantity", Toast.LENGTH_LONG).show()
            return false
        }
        if (etHouseNumber.text!!.isEmpty()) {
            Toast.makeText(this, "Enter shipping address", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun setTotalAmount() {
        tvTotal.text = "Total Amount :: $totalPrice Rs"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}