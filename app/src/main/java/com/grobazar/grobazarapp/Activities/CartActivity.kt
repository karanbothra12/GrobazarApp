package com.grobazar.grobazarapp.Activities

import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grobazar.grobazarapp.Adpaters.CartAdapter
import com.grobazar.grobazarapp.Adpaters.SwipeAdapter
import com.grobazar.grobazarapp.Common.Cart
import com.grobazar.grobazarapp.Food
import com.grobazar.grobazarapp.R
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.cart.*
import java.lang.Exception


class CartActivity : AppCompatActivity() {
var cartlist=ArrayList<Food>()
var cart= Cart()
    var carttotal=0
    var cartAdapter:CartAdapter?=null
    private lateinit var cartlistunique:ArrayList<Food>
var itemcount=0

    private var recyclerView: RecyclerView? = null
    private var imageModelArrayList: ArrayList<Food>? = null
    private var adapter: SwipeAdapter? = null
    private val p = Paint()

    override fun onCreate(savedInstanceState: Bundle?) {
       try {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart)
        recyclerView = findViewById(R.id.listCart) as RecyclerView

        imageModelArrayList = populateList()

        adapter = SwipeAdapter(this, imageModelArrayList!!)


        adapter!!.setOnItemClickListener(object :SwipeAdapter.OnItemClickListener
        {
            override fun onItemClick(position: Int) {

            }

            override fun onitemcountchanged(position: Int, number: String, oldvalue: String) {
                var cartlist=getcartlist()

                var foodpos=cartlistunique!!.get(position)


                println("on item count changed called"+ number)
                var found=0
                for( i in 0..cartlist.size-1)
                {

                    if(cartlist[i].name==foodpos.name)
                    {
                        println("item found")

                        cartlist[i].cart=number
                        cartlistunique[i].cart=number

                        println("old value is "+oldvalue)
                        println("new value is "+number)



                        var discountedprice=cartlist[i].price!!.toInt()-((cartlist[i].price!!.toInt()*cartlist[i].discount!!.toInt()/100))
                        carttotal=carttotal-(oldvalue.toInt()*discountedprice)
                        carttotal=carttotal+(number.toInt()*discountedprice)

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
                total.text="₹ "+carttotal.toString()

                totald.text="₹ "+carttotal.toString()

                cartitemcount.text=(cartitemcount.text.toString().toInt()+ number.toInt()-oldvalue.toInt()).toString()


            }

            override fun onDeleteClick(position: Int) {
                val deletedModel = imageModelArrayList!![position]
                adapter!!.removeItem(position)
                cartlistunique.removeAt(position)
                saveCartList(cartlistunique)
                cartlistunique=getcartlist()
                carttotal=0
                itemcount=0


                println("on delete clicked")
                for (i in 0..cartlistunique.size-1)
                {
                    var discountedprice=cartlistunique[i].price!!.toInt()-((cartlistunique[i].price!!.toInt()*cartlistunique[i].discount!!.toInt()/100))

                    carttotal=carttotal+( cartlistunique[i].cart!!.toInt()!! * discountedprice)
                    itemcount=itemcount+ cartlistunique[i].cart!!.toInt()

                }
                total.text="₹ "+carttotal.toString()

                totald.text="₹ "+carttotal.toString()


                cartitemcount.text=itemcount.toString()
                if(itemcount==0)
                {
                    emptycartlayout.visibility= View.VISIBLE
                    cardView.visibility=View.INVISIBLE
                    cardview2.visibility=View.INVISIBLE

                    cd1.visibility=View.INVISIBLE



                }



            }
        })

        recyclerView!!.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        enableSwipe()



     cartlistunique=getcartlist()

        cartlist=getcartlist()
        for (i in 0..cartlistunique.size-1)
        {
            println(i.toString()+cartlistunique[i].name)
        }


        for (i in 0..cartlistunique.size-1)
        {
            var discountedprice=cartlistunique[i].price!!.toInt()-((cartlistunique[i].price!!.toInt()*cartlistunique[i].discount!!.toInt()/100))

            carttotal=carttotal+( cartlistunique[i].cart!!.toInt()!! * discountedprice)

        }
        for (i in 0..cartlistunique.size-1)
        {

            itemcount=itemcount+ cartlistunique[i].cart!!   .toInt()

        }

        total.text="₹ "+carttotal.toString()

        totald.text="₹ "+carttotal.toString()
        cartitemcount.text=itemcount.toString()
        cartAdapter=CartAdapter( cartlistunique)
//
//        listCart.adapter=cartAdapter
//        listCart.layoutManager=LinearLayoutManager(this)

        cartAdapter!!.setOnItemClickListener(object :CartAdapter.OnItemClickListener{
            override fun onitemcountchanged(position: Int, number: String,oldvalue:String) {

            }

            override fun onItemClick(position: Int) {
            }

            override fun onDeleteClick(position: Int) {


            }
        })



        btnPlaceOrder.setOnClickListener {


         if(getcartlist().size!=0) {

             var intent = Intent(this, Checkout::class.java)
             intent.putExtra("totalamount", carttotal.toString())
             intent.putExtra("itemcount", cartitemcount.text.toString())
             startActivity(intent)
         }
            else
         {
             Toast.makeText(this,"Your Cart is Empty",Toast.LENGTH_LONG).show()
         }
        }
        if( getcartlist().size==0)
        {
            emptycartlayout.visibility= View.VISIBLE
            cardView.visibility=View.INVISIBLE
            cardview2.visibility=View.INVISIBLE

            cd1.visibility=View.INVISIBLE


        }
        else
        {
            emptycartlayout.visibility= View.INVISIBLE
            cd1.visibility=View.VISIBLE
            cardView.visibility=View.VISIBLE
            cardview2.visibility=View.VISIBLE

        }


       }
       catch (e:Exception)
       {
           e.printStackTrace()
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






    private fun enableSwipe() {
        val simpleItemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition

                    if (direction == ItemTouchHelper.LEFT) {
                        val deletedModel = imageModelArrayList!![position]
                        adapter!!.removeItem(position)
                        cartlistunique.removeAt(position)
                        saveCartList(cartlistunique)
                        cartlistunique=getcartlist()
                        carttotal=0
                        itemcount=0


                        for (i in 0..cartlistunique.size-1)
                        {
                            var discountedprice=cartlistunique[i].price!!.toInt()-((cartlistunique[i].price!!.toInt()*cartlistunique[i].discount!!.toInt()/100))

                            carttotal=carttotal+( cartlistunique[i].cart!!.toInt()!! * discountedprice)
                            itemcount=itemcount+ cartlistunique[i].cart!!.toInt()

                        }
                        total.text="₹ "+carttotal.toString()

                        totald.text="₹ "+carttotal.toString()


                        cartitemcount.text=itemcount.toString()


                        if(itemcount==0)
                        {
                            emptycartlayout.visibility= View.VISIBLE
                            cardView.visibility=View.INVISIBLE
                            cardview2.visibility=View.INVISIBLE

                            cd1.visibility=View.INVISIBLE



                        }

                        // showing snack bar with Undo option


                        val snackbar = Snackbar.make(
                            window.decorView.rootView,
                            " removed from Recyclerview!",
                            Snackbar.LENGTH_LONG
                        )
                        snackbar.setAction("UNDO") {
                            // undo is selected, restore the deleted item
                            adapter!!.restoreItem(deletedModel, position)
                        }
                        snackbar.setActionTextColor(Color.YELLOW)
                        snackbar.show()
                    } else {
                        val deletedModel = imageModelArrayList!![position]
                        adapter!!.removeItem(position)
                        cartlistunique.removeAt(position)
                        saveCartList(cartlistunique)
                        cartlistunique=getcartlist()
                        carttotal=0
                        itemcount=0


                        for (i in 0..cartlistunique.size-1)
                        {
                            var discountedprice=cartlistunique[i].price!!.toInt()-((cartlistunique[i].price!!.toInt()*cartlistunique[i].discount!!.toInt()/100))

                            carttotal=carttotal+( cartlistunique[i].cart!!.toInt()!! * discountedprice)
                            itemcount=itemcount+ cartlistunique[i].cart!!.toInt()

                        }
                        total.text="₹ "+carttotal.toString()

                        totald.text="₹ "+carttotal.toString()


                        cartitemcount.text=itemcount.toString()


                        if(itemcount==0)
                        {
                            emptycartlayout.visibility= View.VISIBLE
                            cardView.visibility=View.INVISIBLE
                            cardview2.visibility=View.INVISIBLE

                            cd1.visibility=View.INVISIBLE



                        }

                        // showing snack bar with Undo option
                        val snackbar = Snackbar.make(
                            window.decorView.rootView,
                            " removed from Recyclerview!",
                            Snackbar.LENGTH_LONG
                        )
                        snackbar.setAction("UNDO") {
                            // undo is selected, restore the deleted item
                            adapter!!.restoreItem(deletedModel, position)
                        }
                        snackbar.setActionTextColor(Color.YELLOW)
                        snackbar.show()
                    }
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {

                    val icon: Bitmap
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                        val itemView = viewHolder.itemView
                        val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                        val width = height / 3

                        if (dX > 0) {
                            p.color = Color.parseColor("#D32F2F")
                            val background =
                                RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                            c.drawRect(background, p)
                            icon = BitmapFactory.decodeResource(resources, R.drawable.delete)
                            val icon_dest = RectF(
                                itemView.left.toFloat() + width,
                                itemView.top.toFloat() + width,
                                itemView.left.toFloat() + 2 * width,
                                itemView.bottom.toFloat() - width
                            )
                            c.drawBitmap(icon, null, icon_dest, p)
                        } else {
                            p.color = Color.parseColor("#D32F2F")
                            val background = RectF(
                                itemView.right.toFloat() + dX,
                                itemView.top.toFloat(),
                                itemView.right.toFloat(),
                                itemView.bottom.toFloat()
                            )
                            c.drawRect(background, p)
                            icon = BitmapFactory.decodeResource(resources, R.drawable.delete)
                            val icon_dest = RectF(
                                itemView.right.toFloat() - 2 * width,
                                itemView.top.toFloat() + width,
                                itemView.right.toFloat() - width,
                                itemView.bottom.toFloat() - width
                            )
                            c.drawBitmap(icon, null, icon_dest, p)
                        }
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun populateList(): ArrayList<Food> {

        val list = ArrayList<Food>()

        cartlistunique=getcartlist()
        for (i in 0..cartlistunique.size-1) {
            var imageModel = Food()

            imageModel=cartlistunique[i]
            imageModel.name=cartlistunique[i].name
            imageModel.image=cartlistunique[i].image

            imageModel.cart=cartlistunique[i].cart

            imageModel.discount=cartlistunique[i].discount
            imageModel.price=cartlistunique[i].price


            list.add(imageModel)
        }

        return list
    }
}




