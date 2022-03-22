package com.example.fishthibaker

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fishthibaker.adapter.ProductAdapter
import com.example.fishthibaker.model.ProductModel
import com.example.fishthibaker.ui.AddProductActivity
import com.example.fishthibaker.util.Constant
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class AdminHomeFragment(var mContext: Context) : Fragment() {

    override fun onResume() {
        super.onResume()
//        adapter = ProductAdapter(productList)
        getProductList()
    }

    var fabAdd: FloatingActionButton? = null
    val db = FirebaseFirestore.getInstance()
    var productList: ArrayList<ProductModel> = ArrayList()
    lateinit var adapter: ProductAdapter

    var dialog: ProgressDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = ProgressDialog(mContext)
        dialog!!.setMessage("Please Wait")
        dialog!!.setCancelable(false)

        adapter = ProductAdapter(productList,true, isCart = false, mContext = mContext)

        val rvProduct = view.findViewById<RecyclerView>(R.id.rvProduct)
        rvProduct.layoutManager = LinearLayoutManager(mContext)

        rvProduct.adapter = adapter
        fabAdd = view.findViewById(R.id.fabAdd)
        fabAdd!!.setOnClickListener {
            startActivity(Intent(mContext, AddProductActivity::class.java))
        }
    }

    private fun getProductList() {
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
            }

            dialog!!.dismiss()
        }.addOnFailureListener {
            it.printStackTrace()
            dialog!!.dismiss()
        }
    }
}