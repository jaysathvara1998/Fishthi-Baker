package com.example.fishthibaker.ui

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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

class PaymentActivity : AppCompatActivity(), PaymentResultWithDataListener,
    ExternalWalletListener,
    DialogInterface.OnClickListener {

    lateinit var btn: Button
    private lateinit var alertDialogBuilder: AlertDialog.Builder
    var orderModel: OrderModel? = null
    var model: ProductModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        orderModel = intent.getParcelableExtra("order") as OrderModel?
        model = intent.getSerializableExtra("product") as ProductModel?

        Checkout.preload(applicationContext)
        alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Payment Result")
        alertDialogBuilder.setCancelable(true)
        alertDialogBuilder.setPositiveButton("Ok", this)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        btn = findViewById(R.id.btnSubmit)
        btn.setOnClickListener {
            val rb = radioGroup.findViewById(radioGroup.checkedRadioButtonId) as RadioButton
            if (rb.text.toString() == "Cash on Delivery") {
                placeOrder()
            } else {
                startPayment()
            }
        }

        radioGroup.setOnCheckedChangeListener { p0, p1 ->
            val rb: RadioButton = p0!!.findViewById(p1)
        }
    }

    private fun startPayment() {

        val activity: Activity = this
        val co = Checkout()
        co.setKeyID("rzp_test_r6tx3BZNyECsW4")

        try {
            val options = JSONObject()

            options.put("name", "Fishthi Baker")
            options.put("description", "${model!!.name}")
            options.put("theme.color", "#E65385")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency", "INR")
            options.put("amount", "${(orderModel!!.totalAmount * 100)}")
            options.put("send_sms_hash", true)
            options.put("prefill.email", "fishthibaker@gmail.com")
            options.put("prefill.contact", "9988776655")
            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        try {
            alertDialogBuilder.setMessage("Payment Successful : Payment ID: $p0\nPayment Data: ${p1?.data}")
            alertDialogBuilder.show()
            placeOrder()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun placeOrder() {
        val db = FirebaseFirestore.getInstance()

        db.collection(Constant.ORDER_COLLECTION).add(orderModel!!).addOnSuccessListener {
            val id = it.id
            it.update("id", id)
            Toast.makeText(this, "Order place successfully", Toast.LENGTH_LONG).show()
            val intent = Intent(
                applicationContext,
                UserHomeActivity::class.java
            )
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }.addOnFailureListener {
            it.printStackTrace()
        }
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        try {
            alertDialogBuilder.setMessage("Payment Failed : Payment Data: ${p2?.data}")
            alertDialogBuilder.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onExternalWalletSelected(p0: String?, p1: PaymentData?) {
        try {
            alertDialogBuilder.setMessage("External wallet was selected : Payment Data: ${p1?.data}")
            alertDialogBuilder.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(p0: DialogInterface?, p1: Int) {

    }
}