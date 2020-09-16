package com.grobazar.grobazarapp.Adpaters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.grobazar.grobazarapp.Banner
import com.grobazar.grobazarapp.R
import com.squareup.picasso.Picasso


class BannerAdapter(val imagelist: ArrayList<Banner>) : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.bannerlayout, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(imagelist[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return imagelist.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(banner: Banner) {

            val image=itemView.findViewById<ImageView>(R.id.bannerimage)

            Picasso.get().load(banner.image).fit().into(image)

            println(banner.image)



        }
    }
}