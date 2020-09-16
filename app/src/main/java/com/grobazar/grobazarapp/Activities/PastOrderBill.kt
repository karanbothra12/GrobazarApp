package com.grobazar.grobazarapp.Activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.grobazar.grobazarapp.*
import com.grobazar.grobazarapp.Adpaters.BillAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_bill.billfood
import kotlinx.android.synthetic.main.activity_bill.billquantitytotal
import kotlinx.android.synthetic.main.activity_bill.billtotalamount
import kotlinx.android.synthetic.main.activity_bill.datebill
import kotlinx.android.synthetic.main.activity_bill.invoiceno
import kotlinx.android.synthetic.main.activity_bill.restname
import kotlinx.android.synthetic.main.activity_bill.subtotaltext
import kotlinx.android.synthetic.main.activity_bill.totaltext
import kotlinx.android.synthetic.main.activity_bill.totalwords
import kotlinx.android.synthetic.main.activity_past_order_bill.*

class PastOrderBill : AppCompatActivity() {
    private lateinit var appExecutors: AppExecutors
    private var useremailid: String? = null
    private lateinit var foodList: ArrayList<Food>
    private lateinit var billAdapter: BillAdapter
    var order: Orderdetail? = null

    var foodlistview = ArrayList<Food>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_past_order_bill)

        foodList = ArrayList<Food>()
        appExecutors = AppExecutors()
        var timestamp: String = "Bill"

        billAdapter = BillAdapter(foodList, this)
        billfood.adapter = billAdapter
        billfood.layoutManager = LinearLayoutManager(this)
        billfood.addItemDecoration(ItemDecoration(0))
        if (intent.extras != null) {

            timestamp = intent.getStringExtra("timestamp")
        }
        loaditems(timestamp)


        var myref = FirebaseDatabase.getInstance().getReference("Restaurant")
        myref.child("01").child("Requests").child(timestamp)
            .addValueEventListener(object : ValueEventListener {


                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(this@PastOrderBill, "Something went wrong", Toast.LENGTH_LONG)
                        .show()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    var orderdetail = dataSnapshot.getValue(Orderdetail::class.java)

                    if (orderdetail != null) {
                        order = orderdetail

                        println("pdf method is called")
                    }

                }
            })
        myref.child("01").child("Requests").child(timestamp).child("foods")
            .addValueEventListener(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (snapshot in dataSnapshot.children) {
                        var food = snapshot.getValue(Food::class.java)

                        if (food != null) {
                            foodlistview.add(food)

                        }

                    }


                }
            })


    }

    private fun loaditems(timestamp: String) {
        var myref = FirebaseDatabase.getInstance().getReference("Restaurant")
        myref.child("01").child("Requests").child(timestamp)
            .addValueEventListener(object : ValueEventListener {


                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(this@PastOrderBill, "Something went wrong", Toast.LENGTH_LONG)
                        .show()
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    var orderdetail = dataSnapshot.getValue(Orderdetail::class.java)


                    restname.text = orderdetail!!.restuarnatname

                    billquantitytotal.text = orderdetail!!.itemtotal
                    billtotalamount.text = "₹ "+orderdetail!!.total

                    subtotaltext.text = "₹ "+orderdetail!!.total
                    totaltext.text = "₹ "+orderdetail!!.total

                    var currency = EnglishNumberToWords()

                    totalwords.text =
                        StringCapital.capital(currency.convert(orderdetail.total!!.toLong()))
                    datebill.text = "Date: " + orderdetail.date

                    invoiceno.text = "Invoice No: " + timestamp

                    orderotppast.text="Order Otp: "+orderdetail.otp

                    button.setOnClickListener {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:"+orderdetail.delvphone)
                        startActivity(intent)
                    }
                }
            })

        myref.child("01").child("Requests").child(timestamp).child("foods")
            .addValueEventListener(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (snapshot in dataSnapshot.children) {
                        var food = snapshot.getValue(Food::class.java)

                        if (food != null) {
                            foodList.add(food)
                            billAdapter.notifyDataSetChanged()

                        }

                    }


                }
            })



    }
}
