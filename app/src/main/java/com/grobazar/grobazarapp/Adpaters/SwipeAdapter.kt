package com.grobazar.grobazarapp.Adpaters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.grobazar.grobazarapp.Food
import com.grobazar.grobazarapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cart_layout.view.*
import java.util.ArrayList


class SwipeAdapter(ctx: Context, private val imageModelArrayList: ArrayList<Food>) :
    RecyclerView.Adapter<SwipeAdapter.ViewHolder>() {
    private var mListener: SwipeAdapter.OnItemClickListener? = null

    private val inflater: LayoutInflater


    init {

        inflater = LayoutInflater.from(ctx)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)

        fun onitemcountchanged(position: Int,number:String,oldvalue:String)

        fun onDeleteClick(position: Int)
    }

    fun removeItem(position: Int) {
        imageModelArrayList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, imageModelArrayList.size)
    }

    fun restoreItem(model: Food, position: Int) {
        imageModelArrayList.add(position, model)
        // notify item added by position
        notifyItemInserted(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = inflater.inflate(R.layout.cart_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.bindItems(imageModelArrayList[position],mListener!!)



    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(cartfood: Food,listener: OnItemClickListener) {

            val image = itemView.findViewById<ImageView>(R.id.cart_image)
            Picasso.get().load(cartfood.image).into(image)
            itemView.cart_item_name.text = cartfood.name

            itemView.btn_quantity.setNumber(cartfood.cartitem.toString())



      //      itemView.cart_item_price.text=(cartfood.price!!-((cartfood.price!!* cartfood.discount!!)/100 )).toString()

//            itemView.btn_quantity.setNumber(cartfood.cartitem.toString())
//
//            itemView.cart_item_price.text =
//                (cartfood.price!! - ((cartfood.price!! * cartfood.discount!!) / 100)).toString()



             itemView.cart_item_actual_price.text =
            "₹ "+ (cartfood.price!! - ((cartfood.price!! * cartfood.discount!!) / 100)).toString()




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

            itemView.cartdelete.setOnClickListener(object:View.OnClickListener
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
            })





        }
    }


}