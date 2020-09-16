package com.grobazar.grobazarapp.Adpaters
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.grobazar.grobazarapp.Activities.FoodDetail
import com.grobazar.grobazarapp.Food
import com.grobazar.grobazarapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.food_item.view.*


class FoodListAdapter(val imagelist: ArrayList<Food>,var context: Context) : RecyclerView.Adapter<FoodListAdapter.ViewHolder>() {


    private var mListener: FoodListAdapter.OnItemClickListener? = null


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(imagelist[position],mListener!!)

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
        val v = LayoutInflater.from(parent.context).inflate(R.layout.food_item, parent, false)

        return ViewHolder(v,context)
    }

    //this method is binding the data on the list

    //this method is giving the size of the list
    override fun getItemCount(): Int {

        return imagelist.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View,context1: Context) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(food: Food,listener: FoodListAdapter.OnItemClickListener) {

            var fooddiscount=0
            itemView.findViewById<TextView>(R.id.food_detail).text=food.name
            if(food.discount!=0 && food.discount!=null) {
                itemView.findViewById<TextView>(R.id.food_discount).text = food.discount.toString()+" % off"
                itemView.findViewById<TextView>(R.id.food_discount).visibility=View.VISIBLE
                fooddiscount=(food.price!!.toInt() * food.discount!!.toInt())/100
                itemView.findViewById<TextView>(R.id.fooddiscountprice).text = "₹"+(food.price!! - fooddiscount).toString()
                itemView.findViewById<TextView>(R.id.fooddiscountprice).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.food_price).text = "₹"+ food.price.toString()
                itemView.findViewById<TextView>(R.id.food_price).setPaintFlags( itemView.findViewById<TextView>(R.id.food_price).getPaintFlags() or  Paint.STRIKE_THRU_TEXT_FLAG)




            }
           else
            {
                itemView.findViewById<TextView>(R.id.food_price).text = "₹"+food.price.toString()
            }


            val image=itemView.findViewById<ImageView>(R.id.food_image)
            Picasso.get().load(food.image).into(image)


            itemView.btn_quick_cart.setOnClickListener (object :View.OnClickListener {


                override fun onClick(v: View?)
                {
                    itemView.findViewById<TextView>(R.id.btn_quick_cart).visibility=View.GONE
                    itemView.findViewById<Button>(R.id.deletecart).visibility=View.VISIBLE
                    Toast.makeText(itemView.context,food.name+" item added to cart",Toast.LENGTH_LONG).show()

                    food.cart=itemView.food_number.number.toString()




                    if(listener!=null)
                    {
                        var position=adapterPosition
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position)
                        }
                    }

                }



            }


            )



            itemView.findViewById<TextView>(R.id.deletecart).setOnClickListener (object :View.OnClickListener {


                override fun onClick(v: View?)
                {
                    itemView.findViewById<TextView>(R.id.btn_quick_cart).visibility=View.VISIBLE
                    itemView.findViewById<Button>(R.id.deletecart).visibility=View.GONE
                    Toast.makeText(itemView.context,food.name+" item deleted from cart",Toast.LENGTH_LONG).show()


                    if(listener!=null)
                    {
                        var position=adapterPosition
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onDeleteClick(position)
                        }
                    }

                }



            }


            )

            itemView.food_number.setOnValueChangeListener(object:ElegantNumberButton.OnValueChangeListener
            {
                override fun onValueChange(
                    view: ElegantNumberButton?,
                    oldValue: Int,
                    newValue: Int
                ) {
                    if(listener!=null)
                    {
                        var position=adapterPosition
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onitemcountchanged(position,itemView.food_number.number.toString())
                        }
                    }
                }


               })



            itemView.setOnClickListener {

                var intent= Intent(itemView.context, FoodDetail::class.java)
                intent.putExtra("foodname",food.name)
                itemView.context.startActivity(intent)

            }





        }


    }


}