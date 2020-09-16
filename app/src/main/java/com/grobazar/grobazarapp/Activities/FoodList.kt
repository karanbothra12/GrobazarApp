package com.grobazar.grobazarapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.grobazar.grobazarapp.Adpaters.FoodListAdapter
import com.grobazar.grobazarapp.Common.Cart
import com.grobazar.grobazarapp.Food
import com.grobazar.grobazarapp.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_food_list.*
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.view.View
import java.lang.Exception


class FoodList : AppCompatActivity() {

    var cart1= Cart()
    var subcategory:String?=null
    var category:String?=null
    var fooditemlist:ArrayList<Food>?=null

    var adapter:FoodListAdapter?=null
    private lateinit var database:FirebaseDatabase
  private lateinit var myRef:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)




        if(intent.extras!=null)
        {
            subcategory=intent.getStringExtra("subcategory")
            category=intent.getStringExtra("category")
            println("categoryname is"+category)
            println("subcategory is"+subcategory)


        }

        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("Restaurant")

        fooditemlist=ArrayList<Food>()
        var ref=myRef.child("01").child("detail").child("Foods")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println("erooro takes")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                grogif.visibility= View.INVISIBLE

                for (snapshot in dataSnapshot.children)
                {
                    var food=snapshot.getValue(Food::class.java)


                    var foodobject=Food()


                    if(food!=null)
                    {

                        if( food.subCategory.toString()==subcategory)
                        {

                            println("subcategory match")
                            fooditemlist!!.add(food)
                            recycler_food.adapter!!.notifyDataSetChanged()
                        }
                        else
                        {

                        }



                    }



                }
            }
        })
        adapter= FoodListAdapter(fooditemlist!!,this)
        recycler_food.layoutManager=GridLayoutManager(this,2)
        recycler_food.adapter=adapter

        adapter!!.setOnItemClickListener(object : FoodListAdapter.OnItemClickListener{
            override fun onitemcountchanged(position: Int,number:String) {
                var cartlist=getcartlist()

                var foodpos=fooditemlist!!.get(position)


                println("on item count changed called"+ number)
                var found=0
                for( i in 0..cartlist.size-1)
                {

                    if(cartlist[i].name==foodpos.name)
                    {
                        println("item found")

                        cartlist[i].cart=number

                        found=1
                        break

                    }
                }

                if(cartlist!=null) {
                    if(found!=1)
                    {
                        cartlist.add(foodpos)


                    }
                }

                saveCartList(cartlist)


            }

            override fun onItemClick(position: Int) {

                var cartlist=getcartlist()

                var foodpos=fooditemlist!!.get(position)


                var found=0
                for( i in 0..cartlist.size-1)
                {

                    if(cartlist[i].name==foodpos.name)
                    {
                        println("item found")

                        found=1
                        break

                    }
                }

                if(cartlist!=null) {
                   if(found!=1)
                   {
                       cartlist.add(foodpos)


                   }
                   }

                saveCartList(cartlist)

            }

            override fun onDeleteClick(position: Int) {

                var cartlist=getcartlist()

                var foodpos=fooditemlist!!.get(position)
                   println(foodpos.name)
                    if(cartlist!=null) {
                        cartlist.remove(foodpos)
                    }
                var found=0
                for( i in 0..cartlist.size-1)
                {

                    if(cartlist[i].name==foodpos.name)
                    {
                        println("item found")

                        cartlist.removeAt(i)

                        break

                    }
                }


                saveCartList(cartlist)


            }
        })
        cart.setOnClickListener {
            var intent=Intent(this,CartActivity::class.java)


            startActivity(intent)

        }


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
