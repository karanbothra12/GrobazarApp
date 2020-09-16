package com.grobazar.grobazarapp.Adpaters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.grobazar.grobazarapp.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cart_layout.view.*
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton


class CartAdapter(val imagelist: ArrayList<Food>) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var mListener: OnItemClickListener? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(imagelist[position],mListener!!)

    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)

        fun onitemcountchanged(position: Int,number:String,oldvalue:String)

        fun onDeleteClick(position: Int)
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cart_layout, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return imagelist.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(cartfood: Food,listener:OnItemClickListener) {

            val image=itemView.findViewById<ImageView>(R.id.cart_image)
            Picasso.get().load(cartfood.image).into(image)
            itemView.cart_item_name.text=cartfood.name
            itemView.btn_quantity.setNumber(cartfood.cartitem.toString())

            itemView.cart_item_actual_price.text=(cartfood.price!!-((cartfood.price!!* cartfood.discount!!)/100 )).toString()


            itemView.cart_item_price.text=((cartfood.price!!-((cartfood.price!!* cartfood.discount!!)/100 ))*cartfood.cart!!.toInt()).toString()

            itemView.cartdelete.setOnClickListener (object :View.OnClickListener
            {
                override fun onClick(v: View?) {

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

            itemView.btn_quantity.number=cartfood.cart.toString()
            itemView.btn_quantity.setOnValueChangeListener(object: ElegantNumberButton.OnValueChangeListener
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
                            itemView.cart_item_price.text=((cartfood.price!!-((cartfood.price!!* cartfood.discount!!)/100 ))*newValue).toString()


                            listener.onitemcountchanged(position,newValue.toString(),oldValue.toString())
                        }
                    }
                }


            })


        }



    }


}