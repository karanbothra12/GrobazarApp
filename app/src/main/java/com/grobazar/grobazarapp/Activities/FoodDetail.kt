package com.grobazar.grobazarapp.Activities

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.grobazar.grobazarapp.Food
import com.grobazar.grobazarapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.foodinfo.*
import java.lang.Exception

class FoodDetail : AppCompatActivity() {

    var foodname=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.foodinfo)

        if(intent.extras!=null)
        {
            foodname=intent.getStringExtra("foodname")
        }

        var foodob=Food()
        var  database = FirebaseDatabase.getInstance()
        var myRef = database.getReference("Restaurant")

        var ref=myRef.child("01").child("detail").child("Foods")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println("erooro takes")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {


                for (snapshot in dataSnapshot.children)
                {
                    var food=snapshot.getValue(Food::class.java)




                    if(food!=null)
                    {

                        if( food.name.toString()==foodname)
                        {

                            foodob=food
                            food_name.text=foodname

                            food_price1.text=" ₹ "+food.price

                            if(food.discount!=0) {

                              food_discountprice1.visibility= View.VISIBLE
                                food_discountprice1.text=" ₹ "+ (food.price!!.toInt()-((food.price!!.toInt()*food.discount!!.toInt())/100)).toString()
                                food_price1.setPaintFlags( food_price1.getPaintFlags() or  Paint.STRIKE_THRU_TEXT_FLAG)


                                food_descount.text = food.discount.toString()+"% off  "
                            }

                            food_description.text=food.description

                            Picasso.get().load(food.image).into(img_food)


                        }
                    }



                }
            }
        })
        btnCart.setOnClickListener {


            var cartlist=getcartlist()


            btnCart.visibility=View.GONE

            deletebutton.visibility=View.VISIBLE


            var found=0
            for( i in 0..cartlist.size-1)
            {

                if(cartlist[i].name==foodname)
                {
                    println("item found")


                    found=1
                    break

                }
            }

            if(cartlist!=null) {
                if(found!=1)
                {
                    Toast.makeText(this,"Item added to Cart",Toast.LENGTH_LONG).show()
                    cartlist.add(foodob)


                }
            }

            saveCartList(cartlist)

        }


        deletebutton.setOnClickListener {

            var cartlist=getcartlist()

            deletebutton.visibility=View.GONE
            btnCart.visibility=View.VISIBLE
            for( i in 0..cartlist.size-1)
            {

                if(cartlist[i].name==foodname)
                {
                    println("item found")

                    cartlist.removeAt(i)

                    break

                }
            }


            saveCartList(cartlist)

        }

        number_button.setOnValueChangeListener(object:ElegantNumberButton.OnValueChangeListener
        {
            override fun onValueChange(view: ElegantNumberButton?, oldValue: Int, newValue: Int) {
                var cartlist=getcartlist()



                var found=0
                for( i in 0..cartlist.size-1)
                {

                    if(cartlist[i].name==foodname)
                    {
                        println("item found")



                        cartlist[i].cart=newValue.toString()


                        found=1
                        break

                    }
                }

                if(cartlist!=null) {
                    if(found!=1)
                    {
                        cartlist.add(foodob)

                        cartlist[cartlist.size-1].cart=newValue.toString()


                        Toast.makeText(this@FoodDetail,"Item added to Cart",Toast.LENGTH_LONG).show()


                    }
                }

                saveCartList(cartlist)




            }
        })





    }



    private fun getcartlist():ArrayList<Food>
    {
        try {

            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
            val gson = Gson()
            val json = prefs.getString("cartlist", "cart")
            val type = object : TypeToken<ArrayList<Food>>() {

            }.type
            if(json!=null) {
                return gson.fromJson(json, type)
            }
            else
            {

            }
        }catch (e: Exception)
        {
            println(e)
        }
        var arr=ArrayList<Food>()
        return arr

    }
    fun saveCartList(list: ArrayList<Food>) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString("cartlist", json)
        editor.apply()     // This line is IMPORTANT !!!
        println("cartlist stored succesfully")


    }




}
