package com.example.fishthibaker.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fishthibaker.R
import com.example.fishthibaker.model.ProductModel
import com.example.fishthibaker.util.Constant
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AddProductActivity : AppCompatActivity() {

    var isEdit = false
    var model: ProductModel? = null
    var etCategory: AutoCompleteTextView? = null
    var etUnit: AutoCompleteTextView? = null
    var adapter: ArrayAdapter<String>? = null
    var categoryList = arrayOf(
        "Cakes",
        "Chocolates & Nankhatais",
        "Cupcakes",
        "Pastries & Rolls",
        "Designer Cupcake",
        "Donut"
    )

    var unitList = arrayOf(
        "Gram",
        "KG"
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

    lateinit var icProfile: ImageView
    var isImageSelect: Boolean = false
    var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        dialog = ProgressDialog(this)
        dialog!!.setMessage("Please Wait")
        dialog!!.setCancelable(false)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        isEdit = intent.getBooleanExtra("isEdit", false)
        if (isEdit) {
            model = intent.getSerializableExtra("model") as ProductModel?
        }

        tvName = findViewById(R.id.tvName)
        tvDesc = findViewById(R.id.tvDesc)
        tvPrice = findViewById(R.id.tvPrice)
        tvWeight = findViewById(R.id.tvWeight)

        etCategory = findViewById(R.id.etCategory)
        etUnit = findViewById(R.id.etUnit)
        etName = findViewById(R.id.etName)
        etDesc = findViewById(R.id.etDesc)
        etPrice = findViewById(R.id.etPrice)
        etWeight = findViewById(R.id.etWeight)
        icProfile = findViewById(R.id.icProfile)

        btnAdd = findViewById(R.id.btnAdd)

        adapter = ArrayAdapter<String>(this, R.layout.list_item_text, categoryList)
        etCategory!!.setAdapter(adapter)

        adapter = ArrayAdapter<String>(this, R.layout.list_item_text, unitList)
        etUnit!!.setAdapter(adapter)

        if (isEdit) {
            Glide.with(this).load(model!!.image)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image).into(icProfile)

            etName!!.setText(model!!.name)
            etDesc!!.setText(model!!.description)
            etPrice!!.setText(model!!.price)
            etWeight!!.setText(model!!.weight)
            etCategory!!.setText(model!!.category)

            btnAdd!!.text = "Edit Product"
        }

        icProfile.setOnClickListener {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        btnAdd!!.setOnClickListener {
            category = etCategory!!.text.toString()
            name = etName!!.text.toString()
            desc = etDesc!!.text.toString()
            price = etPrice!!.text.toString()
            weight = etWeight!!.text.toString() + " " + etUnit!!.text.toString()
            dialog!!.show()

            if (isImageSelect) {
                val storage = FirebaseStorage.getInstance().reference
                val mStorage = storage.child("product/$name.png")
                mStorage.putFile(uri!!).addOnSuccessListener { task ->
                    task.storage.downloadUrl.addOnSuccessListener {
                        val data =
                            ProductModel(category, desc, "", it.toString(), name, price, weight)
                        addProductToFirebase(data)
                    }
                }
            } else {
                val data = ProductModel(category, desc, "", "", name, price, weight)
                addProductToFirebase(data)
            }
        }
    }

    private fun addProductToFirebase(data: ProductModel) {
        if (isEdit) {
            Log.e("AddProduct", "Product :: ${model!!.id}")
            db.collection(Constant.PRODUCT_COLLECTION).document(model!!.id).set(data)
                .addOnSuccessListener {
                    dialog!!.dismiss()
                    val intent = Intent(
                        applicationContext,
                        AdminHomeActivity::class.java
                    )
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }.addOnFailureListener {
                    it.printStackTrace()
                    dialog!!.dismiss()
                }
        } else {
            db.collection(Constant.PRODUCT_COLLECTION).add(data).addOnSuccessListener {
                val id = it.id
                it.update("id", id)
                dialog!!.dismiss()
                onBackPressed()
            }.addOnFailureListener {
                it.printStackTrace()
                dialog!!.dismiss()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                uri = data?.data!!
                icProfile.setImageURI(uri)
                isImageSelect = true
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}