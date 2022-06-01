package com.example.fishthibaker.ui

import android.app.Activity
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
import com.example.fishthibaker.R
import com.example.fishthibaker.adapter.OrderAdapter
import com.example.fishthibaker.model.OrderModel
import com.example.fishthibaker.model.OrderProductModel
import com.example.fishthibaker.model.ProductModel
import com.example.fishthibaker.util.Constant
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OrderFragment(val mContext: Activity, val isAdmin: Boolean = false) : Fragment() {

    val db = FirebaseFirestore.getInstance()
    var productList: ArrayList<OrderProductModel> = ArrayList()
    val statusList = ArrayList<String>()

    var dialog: ProgressDialog? = null
    var rvProduct: RecyclerView? = null
    var tvSearch: TextInputLayout? = null
    var etSearch: TextInputEditText? = null
    var tvNoData: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = ProgressDialog(mContext)
        dialog!!.setMessage("Please Wait")
        dialog!!.setCancelable(false)

        statusList.clear()
        statusList.add("In-Progress")
        statusList.add("Shipped")
        statusList.add("Delivered")
        statusList.add("Reject")

        rvProduct = view.findViewById(R.id.rvOrder)
        tvSearch = view.findViewById(R.id.tvSearch)
        etSearch = view.findViewById(R.id.etSearch)
        tvNoData = view.findViewById(R.id.tvNoData)

        val adapter = OrderAdapter(productList, mContext = mContext, isAdmin,statusList)

        dialog!!.show()

        val result = if (isAdmin) {
            db.collection(Constant.ORDER_COLLECTION)
                .get()
        } else {
            db.collection(Constant.ORDER_COLLECTION)
                .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser!!.uid)
                .get()
        }

        result.addOnSuccessListener {
            for (item in it.documents) {
                val id = item.data!!["id"] as String
                val houseNo = item.data!!["houseNo"] as String
                val society = item.data!!["society"] as String
                val landmark = item.data!!["landmark"] as String
                val city = item.data!!["city"] as String
                val state = item.data!!["state"] as String
                val phone = item.data!!["phone"] as String?
                val zip = item.data!!["zip"] as String
                val productId = item["productId"] as String
                val userId = item.data!!["userId"] as String
                val quantity = item.data!!["quantity"] as Long
                val totalAmount = item.data!!["totalAmount"] as Long
                val timestamp = item.data!!["timestamp"] as Timestamp?
                var status = item.data!!["status"] as String?
                val desc = item.data!!["desc"] as String?

                if (status == null) {
                    status = "In-Progress"
                }

                val order = OrderModel(
                    id,
                    houseNo,
                    society,
                    landmark,
                    city,
                    state,
                    phone,
                    zip,
                    productId,
                    userId,
                    timestamp,
                    quantity.toInt(),
                    totalAmount.toInt(),
                    status,
                    desc,
                )

                Log.e("OrderFragment", "ProductID :: $productId")
                db.collection(Constant.PRODUCT_COLLECTION).document(productId).get()
                    .addOnSuccessListener { product ->
                        try {
                            if(product.data != null){
                                item.data!!["id"] = productId
                                val products = product.toObject(ProductModel::class.java)
                                Log.e("OrderFragment", "Product :: ${product.data} Order :: $order")
                                val model = OrderProductModel(products!!, order)
                                productList.add(model)

                                adapter.notifyDataSetChanged()
                                manageVisibility()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
            }
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
//            tvSearch!!.visibility = View.VISIBLE
            tvNoData!!.visibility = View.GONE
        }
    }
}