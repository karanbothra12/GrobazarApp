package com.grobazar.grobazarapp.Adpaters

import android.content.Context
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.grobazar.grobazarapp.Category
import com.grobazar.grobazarapp.R
import com.squareup.picasso.Picasso


class CategoryGridAdapter
    (private val context: Context, val imagelist: ArrayList<Category>) : BaseAdapter() {

    override fun getCount(): Int {

        // Number of times getView method call depends upon gridValues.length
        return imagelist.size
    }

    override fun getItem(position: Int): Any? {

        return null
    }

    override fun getItemId(position: Int): Long {

        return 0
    }


    // Number of times getView method call depends upon gridValues.length

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        // LayoutInflator to call external grid_item.xml file

        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        var gridView: View

        if (convertView == null) {

            gridView = View(context)

            // get layout from grid_item.xml ( Defined Below )

            gridView = inflater.inflate(R.layout.categorylayout, null)

            // set value into textview

            val textView = gridView
                .findViewById(R.id.categoryname) as TextView

            textView.text = imagelist[position].name.toString()
            // set image based on selected text

            val imageView = gridView
                .findViewById(R.id.cateogoryimage) as ImageView

            Picasso.get().load(imagelist[position].image.toString()).into(imageView)
        } else {

            gridView = convertView
        }

        return gridView
    }
}