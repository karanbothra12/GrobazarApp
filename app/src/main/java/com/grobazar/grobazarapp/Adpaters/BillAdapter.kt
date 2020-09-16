package com.grobazar.grobazarapp.Adpaters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grobazar.grobazarapp.Food
import com.grobazar.grobazarapp.R
import kotlinx.android.synthetic.main.billfood.view.*


class BillAdapter(val imagelist: ArrayList<Food>,var context: Context) : RecyclerView.Adapter<BillAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(imagelist[position])

    }



    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.billfood, parent, false)

        return ViewHolder(v,context)
    }

    //this method is binding the data on the list

    //this method is giving the size of the list
    override fun getItemCount(): Int {

        return imagelist.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View,context1: Context) : RecyclerView.ViewHolder(itemView) {

        var number=0;

        fun bindItems(food: Food) {


            itemView.billitemname.text=food.name

            var fooddiscountprice=food.price!!-(food.price!!.toInt() * food.discount!!.toInt())/100

            itemView.billpriceunit.text="₹ " +fooddiscountprice.toString()
            itemView.billquantity.text=food.cart.toString()

            itemView.billamount.text="₹"+((fooddiscountprice)*food.cart!!.toInt()).toString()

            number+=1



        }


    }


}