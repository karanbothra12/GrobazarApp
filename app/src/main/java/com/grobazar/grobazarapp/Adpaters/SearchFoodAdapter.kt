package com.grobazar.grobazarapp.Adpaters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grobazar.grobazarapp.Activities.FoodDetail
import com.grobazar.grobazarapp.Food
import com.grobazar.grobazarapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.searchfood.view.*


class SearchFoodAdapter(val imagelist: ArrayList<Food>,var context: Context) : RecyclerView.Adapter<SearchFoodAdapter.ViewHolder>() {


    private var mListener: FoodListAdapter.OnItemClickListener? = null


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.bindItems(imagelist[position],mListener!!)
        holder.bindItems(imagelist[position])

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)

        fun onDeleteClick(position: Int)

        fun onitemcountchanged(position: Int,number:String)
    }
    fun setOnItemClickListener(listener: FoodListAdapter.OnItemClickListener) {
        mListener = listener
    }


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.searchfood, parent, false)

        return ViewHolder(v,context)
    }

    //this method is binding the data on the list

    //this method is giving the size of the list
    override fun getItemCount(): Int {

        return imagelist.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View,context1: Context) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(food: Food) {


            Picasso.get().load(food.image).into(itemView.foodimage)
            itemView.foodname.text=food.name

            itemView.setOnClickListener {

               var intent= Intent(itemView.context,FoodDetail::class.java)
                intent.putExtra("foodname",food.name)
                itemView.context.startActivity(intent)

            }



        }


    }


}