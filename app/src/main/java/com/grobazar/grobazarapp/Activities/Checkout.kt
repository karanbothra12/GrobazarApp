package com.grobazar.grobazarapp.Activities

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.grobazar.grobazarapp.Address
import com.grobazar.grobazarapp.Adpaters.CheckoutAddressadapter
import com.grobazar.grobazarapp.Food
import com.grobazar.grobazarapp.Orderdetail
import com.grobazar.grobazarapp.R
import com.grobazar.grobazarapp.Users
import com.google.android.gms.wallet.PaymentsClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_checkout.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import android.app.Activity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import com.google.android.gms.wallet.WalletConstants

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentDataRequest
import android.os.Build
import android.widget.Button
import androidx.annotation.RequiresApi

class Checkout : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference
    var itemcount:String?=null
    var itemtotal:String?=null

    var addresschoosed:Address?=null
    var addressadapter:CheckoutAddressadapter?=null
    private lateinit var paymentsClient: PaymentsClient

    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 123
    @RequiresApi(Build.VERSION_CODES.N)

    override fun onCreate(savedInstanceState: Bundle?) {

        try {

            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_checkout)
            paymentsClient = Wallet.getPaymentsClient(
                this,
                Wallet.WalletOptions.Builder()
                    .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                    .build()
            )

            chkdone.setOnClickListener {

                placeorder()

            }


            if (intent.extras != null) {
                itemcount = intent.getStringExtra("itemcount")

                itemtotal = intent.getStringExtra("totalamount")

                println(itemtotal)

            }
            getinfo()


            ckprodamt.text = itemtotal.toString()

            if (!(itemtotal!!.toInt() >= 300)) {

                ckdlv.text = 100.toString()

            } else {
                ckdlv.text = 0.toString()
            }

            items.text = itemcount
            ckamt.text = (itemtotal!!.toInt() + ckdlv.text.toString().toInt()).toString()

            rgbillinggroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->


                if (rdibilling.isChecked) {
                    chooseaddr.visibility = View.VISIBLE
                    addressrecycle.visibility = View.VISIBLE


                } else {
                    chooseaddr.visibility = View.GONE
                    addressrecycle.visibility = View.GONE

                }


            })


        }catch (e:java.lang.Exception)
        {
            e.printStackTrace()
        }



    }

   private fun placeorder()
    {




            try {

                var foodlist = getcartlist()
                if (foodlist.size != 0) {

                    var orderdetail = Orderdetail()


                    var userdetail: Users? = null
                    var phone = FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString()
                        .removePrefix("+91")
                    val tsLong = System.currentTimeMillis() / 1000
                    val ts = tsLong.toString()

                    println(ts)
                    database = FirebaseDatabase.getInstance()

                    database = FirebaseDatabase.getInstance()
                    myRef = database.getReference("Restaurant")

                    var ref = database.getReference("User")
                    println(phone)
                    ref.child(phone).addValueEventListener(object : ValueEventListener {


                        override fun onCancelled(p0: DatabaseError) {
                            Toast.makeText(this@Checkout, "Error ", Toast.LENGTH_LONG).show()
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            userdetail = dataSnapshot.getValue(Users::class.java)


                            if (userdetail != null) {
                                orderdetail.address = userdetail!!.address
                                orderdetail.comment = edtComment.text.toString()
                                orderdetail.personname = userdetail!!.name

                                orderdetail.foods = foodlist

                                orderdetail.restuarnatname = userdetail!!.restaurant_Name
                                orderdetail.itemtotal = itemcount
                                orderdetail.total = itemtotal

                                var date = Date() // the date instance
                                var calendar = Calendar.getInstance();
                                calendar.setTime(date);
                                var datetoday =
                                    calendar.get(Calendar.DAY_OF_MONTH).toString() + " " + (calendar.get(
                                        Calendar.MONTH
                                    ) + 1).toString() + " " + calendar.get(Calendar.YEAR)

                                orderdetail.phone = phone
                                orderdetail.status = "Placed"
                                orderdetail.timestamp = ts
                                orderdetail.date = datetoday

                                orderdetail.delvphone=""
                                val randomPIN = (Math.random() * 9000).toInt() + 1000
                                var otp = randomPIN.toString()

                                println("otp is" + otp)
                                orderdetail.otp = otp
                                myRef.child("01").child("Requests").child(ts).setValue(orderdetail)


                                Toast.makeText(
                                    this@Checkout,
                                    "Order Placed Succesfully",
                                    Toast.LENGTH_LONG
                                ).show()

                                var intent = Intent(this@Checkout, Bill::class.java)
                                intent.putExtra("name", orderdetail.personname)
                                intent.putExtra("date", orderdetail.date)
                                intent.putExtra("totalitem", orderdetail.itemtotal)
                                intent.putExtra("price", orderdetail.total)
                                intent.putExtra("timestamp", orderdetail.timestamp)
                                intent.putExtra("useremail",userdetail!!.email)


                                var arrlist:ArrayList<Food>?=null
                                saveCartList(arrlist)
                                startActivity(intent)


                                finish()


                            }
                        }
                    })


                } else {
                    Toast.makeText(this, "Your cart is empty ", Toast.LENGTH_LONG).show()

                }

            } catch (e: Exception) {
                Toast.makeText(this, "Error ", Toast.LENGTH_LONG).show()
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
    fun saveCartList(list: ArrayList<Food>?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString("cartlist", json)
        editor.apply()     // This line is IMPORTANT !!!
        println("cartlist stored succesfully")
    }




    private fun getinfo()
    {




        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("User")
        var user:Users?=null

        println((FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString()).removePrefix("+91"))
        myRef.child((FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString()).removePrefix("+91")).addValueEventListener(object :ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {
                println("Something is worng")
            }

            override fun onDataChange(datasnapshot: DataSnapshot) {

                user=datasnapshot.getValue(Users::class.java)

                //println(user!!.phoneno)



                checkname.text=user!!.name
                checkphno.text=user!!.phoneno
                checkemail.text=user!!.email
                var addresslist=ArrayList<Address>()
                var address=Address()

                address.addressname=user!!.address
                addresslist.add(address)

            //        addresslist.add(address1)

                addressadapter= CheckoutAddressadapter(addresslist)
                addressrecycle.adapter=addressadapter
                addressrecycle.layoutManager=LinearLayoutManager(applicationContext)
                addressadapter!!.setOnItemClickListener(object :CheckoutAddressadapter.OnItemClickListener{

                    override fun onItemClick(position: Int) {
                      println("itemclicked")
                        addresschoosed=addresslist[position]



                    }
                })


            }



        })

    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun possiblyShowGooglePayButton() {
        val isReadyToPayJson = GooglePay.isReadyToPayRequest
        if (!isReadyToPayJson.isPresent) {
            return
        }
        val request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString()) ?: return
        val task = paymentsClient.isReadyToPay(request)
        task.addOnCompleteListener(
            OnCompleteListener<Boolean> { task ->

                try {
                    val result = task.getResult(ApiException::class.java)
                    if (result!!) {
                        // show Google as a payment option
                        var button:Button=findViewById(R.id.chkdone)
                        button.setOnClickListener{
                            println("online bill choosed")
                            requestPayment()
                        }

                    }

                }
                catch (exception: ApiException) {
                    // handle developer errors
                }
            })

    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun requestPayment() {
        println("request function is running")
        val paymentDataRequestJson = GooglePay.paymentDataRequest
        if (!paymentDataRequestJson.isPresent) {
            return
        }
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString())
        if (request != null) {
            AutoResolveHelper.resolveTask(
                paymentsClient.loadPaymentData(request), this, LOAD_PAYMENT_DATA_REQUEST_CODE
            )
        }
    }

    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            // value passed in AutoResolveHelper
            LOAD_PAYMENT_DATA_REQUEST_CODE-> {
                when(resultCode) {
                    Activity . RESULT_OK -> {
                        var paymentData = PaymentData.getFromIntent(data!!);
                        var json: String = paymentData!!.toJson();
                        // if using gateway tokenization, pass this token without modification
                        var paymentMethodData = JSONObject(json)
                            .getJSONObject("paymentMethodData")
                        var paymentToken = paymentMethodData
                            .getJSONObject("tokenizationData")
                            .getString("token");


                            println("Paid succesfullt")

                    }
                    Activity . RESULT_CANCELED ->
                    {


                        println("payment canceld")

                    }
                    AutoResolveHelper . RESULT_ERROR ->
                    {

                        println("Error takes")

                        var status = AutoResolveHelper . getStatusFromIntent (data);}




                }

            }

        }
    }




}


