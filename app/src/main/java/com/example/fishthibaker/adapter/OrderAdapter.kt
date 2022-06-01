package com.example.fishthibaker.adapter

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fishthibaker.R
import com.example.fishthibaker.model.OrderProductModel
import com.example.fishthibaker.util.Constant
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DateFormat


class OrderAdapter(
    private val mList: List<OrderProductModel>,
    private val mContext: Activity,
    val isAdmin: Boolean,
    val statusList: ArrayList<String>
) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_order, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = mList[position]
        var selectedStatus = model.order.status

        val adapter =
            ArrayAdapter(mContext, R.layout.support_simple_spinner_dropdown_item, statusList)

        holder.spStatus.adapter = adapter

        val index = adapter.getPosition(selectedStatus)
        holder.spStatus.setSelection(index)

        holder.spStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                selectedStatus = adapterView!!.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        holder.btnUpdate.setOnClickListener {
            FirebaseFirestore.getInstance().collection(Constant.ORDER_COLLECTION)
                .document(model.order.id).update("status", selectedStatus).addOnSuccessListener {
                Toast.makeText(mContext, "Status updated successfully", Toast.LENGTH_LONG).show()
            }
        }

        holder.tvName.text = model.product.name
        if (isAdmin) {
            holder.layoutStatus.visibility = View.VISIBLE
            holder.tvAddress.text =
                "ShippingAddress : ${model.order.houseNo}, ${model.order.society}, ${model.order.landmark}, ${model.order.city}- ${model.order.zip}"
            holder.tvMobile.visibility = View.VISIBLE
            holder.tvMobile.text = "ContactNO : ${model.order.phone}"
            if (model.order.phone != null && model.order.phone.isNotEmpty()) {
                holder.tvMobile.setOnClickListener {
                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            mContext,
                            arrayOf(Manifest.permission.CALL_PHONE),
                            100
                        )
                    } else {
                        val intent = Intent(Intent.ACTION_CALL)

                        intent.data = Uri.parse("tel:${model.order.phone}")
                        mContext.startActivity(intent)
                    }
                }
            }
        } else {
            holder.layoutStatus.visibility = View.GONE
            holder.tvAddress.text = "ShippingAddress : ${model.order.houseNo}"
            holder.tvMobile.visibility = View.GONE
        }
        holder.tvDesc.text = "Name on Cake : ${model.order.desc}"
        holder.tvWeight.text = "Weight : ${model.product.weight}"
        holder.tvQuantity.text = "Quantity : ${model.order.quantity}"
        holder.tvTotalAmount.text = "Amount : ${model.order.totalAmount} Rs"
        holder.tvPrice.text = "Price : ${model.product.price} Rs"
        if (model.order.status == null) {
            model.order.status = "In-Progress"
        }
        holder.tvStatus.text = model.order.status
        if (model.order.timestamp == null) {
            model.order.timestamp = Timestamp.now()
        }
        val date = model.order.timestamp!!.toDate()
        val stDate = DateFormat.getDateInstance().format(date)
        holder.tvDate.text = "OrderDate : $stDate"
        if (model.product.image.isNotEmpty()) {
            Glide.with(mContext).load(model.product.image)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image).into(holder.icProductImage)
        }

        if (!isAdmin) {
            holder.tvAddress.visibility = View.GONE
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvWeight: TextView = itemView.findViewById(R.id.tvWeight)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val tvTotalAmount: TextView = itemView.findViewById(R.id.tvTotalAmount)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
        val tvAddress: TextView = itemView.findViewById(R.id.tvHouseNumber)
        val tvMobile: TextView = itemView.findViewById(R.id.tvMobile)
        val spStatus: AppCompatSpinner = itemView.findViewById(R.id.spStatus)
        val btnUpdate: Button = itemView.findViewById(R.id.btnUpdate)
        val layoutStatus: LinearLayout = itemView.findViewById(R.id.layoutStatus)

        val layout: LinearLayout = itemView.findViewById(R.id.layout)
        val icProductImage: ImageView = itemView.findViewById(R.id.icProductImage)
    }
}