package com.example.fishthibaker.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.fishthibaker.R
import com.example.fishthibaker.model.ProductModel
import com.example.fishthibaker.util.Constant
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore

class AddProductActivity : AppCompatActivity() {

    var etCategory: AutoCompleteTextView? = null
    var adapter: ArrayAdapter<String>? = null
    var categoryList = arrayOf(
        "Cakes",
        "Chocolates & Nankhatais",
        "Cupcakes",
        "Pastries & Rolls",
        "Designer Cupcake",
        "Donut"
    )
    var tvName: TextInputLayout? = null
    var tvDesc: TextInputLayout? = null
    var tvPrice: TextInputLayout? = null
    var tvWeight: TextInputLayout? = null

    var etName: TextInputEditText? = null
    var etDesc: TextInputEditText? = null
    var etPrice: TextInputEditText? = null
    var etWeight: TextInputEditText? = null

    var btnAdd: Button? = null

    var name: String = ""
    var desc: String = ""
    var price: String = ""
    var weight: String = ""
    var category: String = ""

    var dialog: ProgressDialog? = null
    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        dialog = ProgressDialog(this)
        dialog!!.setMessage("Please Wait")
        dialog!!.setCancelable(false)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        tvName = findViewById(R.id.tvName)
        tvDesc = findViewById(R.id.tvDesc)
        tvPrice = findViewById(R.id.tvPrice)
        tvWeight = findViewById(R.id.tvWeight)

        etCategory = findViewById(R.id.etCategory)
        etName = findViewById(R.id.etName)
        etDesc = findViewById(R.id.etDesc)
        etPrice = findViewById(R.id.etPrice)
        etWeight = findViewById(R.id.etWeight)

        btnAdd = findViewById(R.id.btnAdd)

        adapter = ArrayAdapter<String>(this, R.layout.list_item_text, categoryList)
        etCategory!!.setAdapter(adapter)

        btnAdd!!.setOnClickListener {
            category = etCategory!!.text.toString()
            name = etName!!.text.toString()
            desc = etDesc!!.text.toString()
            price = etPrice!!.text.toString()
            weight = etWeight!!.text.toString()
            dialog!!.show()

            val data = ProductModel(category, desc, "", "", name, price, weight)
            db.collection(Constant.PRODUCT_COLLECTION).add(data).addOnSuccessListener {
                val id = it.id
                print("AddProduct ===> Ref :: ${it}")
                it.update("id",id)
                dialog!!.dismiss()
                onBackPressed()
            }.addOnFailureListener {
                it.printStackTrace()
                dialog!!.dismiss()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}