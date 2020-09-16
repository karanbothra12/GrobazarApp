package com.grobazar.grobazarapp.Adpaters

import com.grobazar.grobazarapp.Interface.ItemClickListener


import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.grobazar.grobazarapp.R

/**
 * Created by vishalverma on 17/01/18.
 */

class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    var txtMenuName: TextView?=null
    var imageView: ImageView?=null

    private var itemClickListener: ItemClickListener? = null

    init {


        txtMenuName = itemView.findViewById(R.id.categoryname) as TextView
        imageView = itemView.findViewById(R.id.cateogoryimage) as ImageView
        itemView.setOnClickListener(this)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun onClick(view: View) {

        itemClickListener!!.onClick(view, adapterPosition, false)
    }

    companion object {

        private val TYPE_HEADER = 0
        private val TYPE_ITEM = 1
    }
}
