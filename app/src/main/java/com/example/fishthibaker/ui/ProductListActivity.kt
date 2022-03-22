package com.example.fishthibaker.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fishthibaker.adapter.ProductAdapter
import com.example.fishthibaker.R
import com.example.fishthibaker.model.ProductModel
import com.example.fishthibaker.util.Constant
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore

class ProductListActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    var category: String = ""
    var productList: ArrayList<ProductModel> = ArrayList()
    var finalProductList: ArrayList<ProductModel> = ArrayList()

    var dialog: ProgressDialog? = null
    var rvProduct: RecyclerView? = null
    var tvSearch: TextInputLayout? = null
    var etSearch: TextInputEditText? = null
    var tvNoData: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        category = intent.getStringExtra("category").toString()

        dialog = ProgressDialog(this)
        dialog!!.setMessage("Please Wait")
        dialog!!.setCancelable(false)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "$category List"

        val adapter = ProductAdapter(productList, false, isCart = false, mContext = this)
        dialog!!.show()
        db.collection(Constant.PRODUCT_COLLECTION).get().addOnSuccessListener {
            productList.clear()

            for (item in it.documents) {

                val category = item.data!!["category"] as String?
                val description = item.data!!["description"] as String?
                val id = item.data!!["id"] as String?
                val image = item.data!!["image"] as String?
                val name = item.data!!["name"] as String?
                val price = item.data!!["price"] as String?
                val weight = item.data!!["weight"] as String?

                if (category == this.category) {
                    productList.add(
                        ProductModel(
                            category,
                            description!!,
                            id!!,
                            image!!,
                            name!!,
                            price!!,
                            weight!!
                        )
                    )

                    finalProductList.add(
                        ProductModel(
                            category,
                            description!!,
                            id!!,
                            image!!,
                            name!!,
                            price!!,
                            weight!!
                        )
                    )
                    adapter.notifyDataSetChanged()
                    manageVisibility()
                }
            }

            dialog!!.dismiss()
        }.addOnFailureListener {
            it.printStackTrace()
            dialog!!.dismiss()
        }

        rvProduct = findViewById(R.id.rvProduct)
        tvSearch = findViewById(R.id.tvSearch)
        etSearch = findViewById(R.id.etSearch)
        tvNoData = findViewById(R.id.tvNoData)

        etSearch!!.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                productList.clear()
                if (s.isEmpty()) {
                    productList.addAll(finalProductList)
                } else {
                    for (category in finalProductList) {
                        if (category.name.contains(s, true)) {
                            productList.add(category)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }
        })

        rvProduct!!.layoutManager = LinearLayoutManager(this)
        rvProduct!!.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun manageVisibility() {
        if (productList.isEmpty()) {
            rvProduct!!.visibility = View.GONE
            tvSearch!!.visibility = View.GONE
            tvNoData!!.visibility = View.VISIBLE
        } else {
            rvProduct!!.visibility = View.VISIBLE
            tvSearch!!.visibility = View.VISIBLE
            tvNoData!!.visibility = View.GONE
        }
    }
}