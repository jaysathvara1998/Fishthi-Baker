package com.example.fishthibaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.example.fishthibaker.adapter.CategoryAdapter
import com.example.fishthibaker.ui.ProductListActivity
import com.google.android.material.textfield.TextInputEditText
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class UserHomeFragment(val mContext: Context) : Fragment() {

    lateinit var gridView: GridView
    lateinit var etSearch: TextInputEditText

    val categoryList: ArrayList<Item> = ArrayList()
    val finalCategoryList: ArrayList<Item> = ArrayList()

    var sampleImages = intArrayOf(
        R.drawable.poster1,
        R.drawable.poster2,
        R.drawable.poster3,
        R.drawable.poster4,
        R.drawable.poster5,
        R.drawable.poster6,
        R.drawable.poster7
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categoryList.add(Item("Cakes", R.drawable.ic_cake))
        categoryList.add(Item("Chocolates & Nankhatais", R.drawable.ic_chocolate))
        categoryList.add(Item("Cupcakes", R.drawable.ic_cupcake))
        categoryList.add(Item("Pastries & Rolls", R.drawable.ic_pastry))
        categoryList.add(Item("Designer Cupcake", R.drawable.ic_designer_cupcake))
        categoryList.add(Item("Donut", R.drawable.ic_donut))

        finalCategoryList.add(Item("Cakes", R.drawable.ic_cake))
        finalCategoryList.add(Item("Chocolates & Nankhatais", R.drawable.ic_chocolate))
        finalCategoryList.add(Item("Cupcakes", R.drawable.ic_cupcake))
        finalCategoryList.add(Item("Pastries & Rolls", R.drawable.ic_pastry))
        finalCategoryList.add(Item("Designer Cupcake", R.drawable.ic_designer_cupcake))
        finalCategoryList.add(Item("Donut", R.drawable.ic_donut))

        return inflater.inflate(R.layout.fragment_user_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridView = view.findViewById(R.id.gvCategory)
        etSearch = view.findViewById(R.id.etSearch)

        val mainAdapter = CategoryAdapter(mContext, categoryList)

        etSearch.addTextChangedListener(object : TextWatcher {

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
                categoryList.clear()
                if (s.isEmpty()) {
                    categoryList.addAll(finalCategoryList)
                } else {
                    for (category in finalCategoryList) {
                        if (category.title.contains(s, true)) {
                            categoryList.add(category)
                        }
                    }
                }
                mainAdapter.notifyDataSetChanged()
            }
        })

        val carouselView = view.findViewById<CarouselView>(R.id.carouselView)

        carouselView.setImageListener(imageListener)
        carouselView.pageCount = sampleImages.size

        gridView.adapter = mainAdapter
        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            val intent = Intent(mContext, ProductListActivity::class.java)
            intent.putExtra("category", categoryList[+position].title)
            mContext.startActivity(intent)
        }
    }

    var imageListener =
        ImageListener { position, imageView -> imageView.setImageResource(sampleImages[position]) }

}

data class Item(
    val title: String,
    @DrawableRes val icon: Int
)