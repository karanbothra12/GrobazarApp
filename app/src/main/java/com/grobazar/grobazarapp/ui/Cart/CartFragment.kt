package com.grobazar.grobazarapp.ui.Cart

import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grobazar.grobazarapp.Activities.Checkout
import com.grobazar.grobazarapp.Adpaters.CartAdapter
import com.grobazar.grobazarapp.Adpaters.SwipeAdapter
import com.grobazar.grobazarapp.Common.Cart
import com.grobazar.grobazarapp.Food
import com.grobazar.grobazarapp.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart.view.*
import java.lang.Exception

class CartFragment : Fragment() {

    var cartlist=ArrayList<Food>()
    var cart= Cart()
    var carttotal=0
    var cartAdapter:CartAdapter?=null
    private lateinit var cartlistunique:ArrayList<Food>
    var itemcount=0

    private lateinit var  recyclerView: RecyclerView
    private var imageModelArrayList: ArrayList<Food>? = null
    private var adapter: SwipeAdapter? = null
    private val p = Paint()

    private lateinit var cartViewModel: CartViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cartViewModel =
            ViewModelProviders.of(this).get(CartViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_cart, container, false)

        recyclerView = root.findViewById(R.id.homecartrecycler) as RecyclerView

        imageModelArrayList = populateList()

        adapter = SwipeAdapter(context!!, imageModelArrayList!!)


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
                root.homecartotal.text= "₹ "+carttotal.toString()

                root.homecarttotald.text= "₹ "+carttotal.toString()

                root.homecartitemcount.text=(root.homecartitemcount.text.toString().toInt()+ number.toInt()-oldvalue.toInt()).toString()


            }

            override fun onDeleteClick(position: Int) {
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
                homecartotal.text= "₹ "+carttotal.toString()

                root.homecarttotald.text="₹ "+carttotal.toString()


                homecartitemcount.text=itemcount.toString()




            }
        })

        recyclerView!!.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        enableSwipe()


        var cartlistunique=getcartlist()

        cartlist=getcartlist()

        for (i in 0..cartlistunique.size-1)
        {
            var discountedprice=cartlistunique[i].price!!.toInt()-((cartlistunique[i].price!!.toInt()*cartlistunique[i].discount!!.toInt()/100))

            carttotal=carttotal+( cartlistunique[i].cart!!.toInt()!! * discountedprice)

        }
        var itemcount=0
        for (i in 0..cartlistunique.size-1)
        {

            itemcount=itemcount+ cartlistunique[i].cart!!   .toInt()

        }

        root.homecartotal.text="₹ "+carttotal.toString()

        root.homecarttotald.text="₹ "+carttotal.toString()
        root.homecartitemcount.text=itemcount.toString()
        if(itemcount==0)
        {
            root.homeemptycartlayout.visibility= View.VISIBLE
            root.cd11.visibility=View.INVISIBLE

            root.homecardView.visibility=View.INVISIBLE
            root.homecardview2.visibility=View.INVISIBLE


        }




        root.homecheckout.setOnClickListener {

           if(getcartlist().size!=0) {
               var intent = Intent(context, Checkout::class.java)
               intent.putExtra("totalamount", carttotal.toString())
               intent.putExtra("itemcount", root.homecartitemcount.text.toString())
               startActivity(intent)
           }
            else
           {
               Toast.makeText(context,"Your cart is Empty",Toast.LENGTH_LONG).show()

           }


        }
        if( getcartlist().size==0)
        {
            root.homeemptycartlayout.visibility= View.VISIBLE
            root.cd11.visibility=View.INVISIBLE

            root.homecardView.visibility=View.INVISIBLE
            root.homecardview2.visibility=View.INVISIBLE


        }
        else
        {
            root.homeemptycartlayout.visibility= View.INVISIBLE
            root.cd11.visibility=View.VISIBLE
            root.homecardView.visibility=View.VISIBLE
            root.homecardview2.visibility=View.VISIBLE

        }

        return root
    }

    private fun getcartlist():ArrayList<Food>
    {
        try {

            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
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
        }catch (e:Exception)
        {
            println(e)
        }
        var arr=ArrayList<Food>()
        return arr
    }
    fun saveCartList(list: ArrayList<Food>) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
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
                        homecartotal.text="₹ "+carttotal.toString()

                        homecartitemcount.text=itemcount.toString()
                        homecarttotald.text="₹ "+carttotal.toString()


                        if(itemcount==0)
                        {
                            homeemptycartlayout.visibility= View.VISIBLE
                            cd11.visibility=View.INVISIBLE

                            homecardView.visibility=View.INVISIBLE
                            homecardview2.visibility=View.INVISIBLE


                        }


                        // showing snack bar with Undo option


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
                        homecartotal.text="₹ "+carttotal.toString()
                        homecarttotald.text="₹ "+carttotal.toString()
                        homecartitemcount.text=itemcount.toString()



                        homecartitemcount.text=itemcount.toString()
                        if(itemcount==0)
                        {
                            homeemptycartlayout.visibility= View.VISIBLE
                            cd11.visibility=View.INVISIBLE

                            homecardView.visibility=View.INVISIBLE
                            homecardview2.visibility=View.INVISIBLE


                        }




                        // showing snack bar with Undo option

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



            list.add(imageModel)
        }

        return list
    }



}