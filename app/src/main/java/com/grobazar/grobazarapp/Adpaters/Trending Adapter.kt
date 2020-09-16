package com.grobazar.grobazarapp.Adpaters
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grobazar.grobazarapp.Activities.FoodDetail
import com.grobazar.grobazarapp.Food
import com.grobazar.grobazarapp.R
import com.squareup.picasso.Picasso


class TrendingAdapter(val imagelist: ArrayList<Food>) : RecyclerView.Adapter<TrendingAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(imagelist[position])
    }



    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.trendinglauout, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return imagelist.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(category: Food) {

            itemView.findViewById<TextView>(R.id.categoryname).text=category.name
            println("trending item name is " +category.name)
            val image=itemView.findViewById<ImageView>(R.id.cateogoryimage)
            Picasso.get().load(category.image).into(image)

         //   itemView.trendprice.text="₹ "+category.price.toString()
            itemView.setOnClickListener {

                var intent= Intent(itemView.context, FoodDetail::class.java)
                intent.putExtra("foodname",category.name)
                itemView.context.startActivity(intent)

            }

        }
    }
}