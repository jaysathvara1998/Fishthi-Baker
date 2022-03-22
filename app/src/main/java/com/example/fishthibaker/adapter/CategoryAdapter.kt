package com.example.fishthibaker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.fishthibaker.Item
import com.example.fishthibaker.R


internal class CategoryAdapter(
    private val context: Context,
    private val categoryList: ArrayList<Item>,
) :
    BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    override fun getCount(): Int {
        return categoryList.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.activity_gridview, null)
        }
        imageView = convertView!!.findViewById(R.id.ivCategory)
        textView = convertView.findViewById(R.id.tvName)

        imageView.setImageResource(categoryList[position].icon)
        textView.text = categoryList[position].title
        return convertView
    }
}