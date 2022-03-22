package com.example.fishthibaker

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fishthibaker.adapter.ProductAdapter
import com.example.fishthibaker.model.CartModel
import com.example.fishthibaker.model.ProductModel
import com.example.fishthibaker.util.Constant
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class CartFragment(val mContext: Context) : Fragment() {

    val db = FirebaseFirestore.getInstance()
    var productList: ArrayList<ProductModel> = ArrayList()
    var finalProductList: ArrayList<ProductModel> = ArrayList()

    var dialog: ProgressDialog? = null
    var rvProduct: RecyclerView? = null
    var tvSearch: TextInputLayout? = null
    var etSearch: TextInputEditText? = null
    var tvNoData: TextView? = null

    val userRef = FirebaseFirestore.getInstance().collection(Constant.USER_COLLECTION)
        .document(FirebaseAuth.getInstance().currentUser!!.uid)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = ProgressDialog(mContext)
        dialog!!.setMessage("Please Wait")
        dialog!!.setCancelable(false)

        rvProduct = view.findViewById(R.id.rvProduct)
        tvSearch = view.findViewById(R.id.tvSearch)
        etSearch = view.findViewById(R.id.etSearch)
        tvNoData = view.findViewById(R.id.tvNoData)

        val adapter = ProductAdapter(productList, false, isCart = true, mContext = mContext)

        dialog!!.show()
        db.collection(Constant.CART_COLLECTION).whereEqualTo("userRef",userRef).get().addOnSuccessListener {
            Log.e("CartModel","DocLength :: ${it.documents.size}")
            productList.clear()
            for (item in it.documents) {

                val user = item.data!!["userRef"] as DocumentReference
                val product = item.data!!["productRef"] as DocumentReference
                val cartModel = CartModel(product,user)

                cartModel.productRef.get().addOnSuccessListener { product ->

                    Log.e("CartModel","Product :: ${product.data}")

                    val category = product.data!!["category"] as String?
                    val description = product.data!!["description"] as String?
                    val id = product.data!!["id"] as String?
                    val image = product.data!!["image"] as String?
                    val name = product.data!!["name"] as String?
                    val price = product.data!!["price"] as String?
                    val weight = product.data!!["weight"] as String?

                    productList.add(
                        ProductModel(
                            category!!,
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
            Log.e("CartModel","ProductSize :: ${productList.size}")

            dialog!!.dismiss()
        }.addOnFailureListener {
            it.printStackTrace()
            dialog!!.dismiss()
        }

        rvProduct!!.layoutManager = LinearLayoutManager(mContext)
        rvProduct!!.adapter = adapter
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