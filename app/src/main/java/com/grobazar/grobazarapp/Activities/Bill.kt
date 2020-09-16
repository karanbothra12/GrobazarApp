package com.grobazar.grobazarapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer
import android.view.View
import java.io.File
import com.hendrix.pdfmyxml.PdfDocument
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grobazar.grobazarapp.*
import com.grobazar.grobazarapp.Adpaters.BillAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_bill.*
import javax.mail.Authenticator
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


import com.grobazar.grobazarapp.R

class Bill : AppCompatActivity() {

    private lateinit var appExecutors:AppExecutors
    private  var useremailid:String?=null
    private lateinit var foodList:ArrayList<Food>
    private lateinit var billAdapter: BillAdapter
    var order:Orderdetail?=null

    var foodlistview=ArrayList<Food>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)



        foodList=ArrayList<Food>()
        appExecutors = AppExecutors()
        var timestamp:String="Bill"

        billAdapter= BillAdapter(foodList,this)
        billfood.adapter=billAdapter
        billfood.layoutManager=LinearLayoutManager(this)
        billfood.addItemDecoration(ItemDecoration(0))
        if(intent.extras!=null)
        {

                useremailid=intent.getStringExtra("useremail")
                timestamp=intent.getStringExtra("timestamp")
        }

        loaditems(timestamp)

        var myref = FirebaseDatabase.getInstance().getReference("Restaurant")
        myref.child("01").child("Requests").child(timestamp)
            .addValueEventListener(object : ValueEventListener {


                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(this@Bill, "Something went wrong", Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    var orderdetail = dataSnapshot.getValue(Orderdetail::class.java)

                    if (orderdetail != null) {
                        order = orderdetail

                        println("pdf method is called")
                        pdf(timestamp)
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



        pdf(timestamp)


    }
    private fun getpdf(timestamp:String)
    {
        val file =
            File(this.getExternalFilesDir(null).toString() + "/" + timestamp)
        val target = Intent(Intent.ACTION_VIEW)
        target.setDataAndType(Uri.fromFile(file), "application/pdf")
        target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

        val intent = Intent.createChooser(target, "Open File")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        try {
            this.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Instruct the user to install a PDF reader here, or something
        }



    }
    private fun sendEmail(timestamp: String){
        appExecutors.diskIO().execute {
            val props = System.getProperties()
            props.put("mail.smtp.host", "smtp.gmail.com")
            props.put("mail.smtp.socketFactory.port", "465")
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            props.put("mail.smtp.auth", "true")
            props.put("mail.smtp.port", "465")

            var session =  Session.getInstance(props,
                object : Authenticator() {
                    //Authenticating the password
                    override fun getPasswordAuthentication(): javax.mail.PasswordAuthentication {
                        return javax.mail.PasswordAuthentication(Credentials.EMAIL, Credentials.PASSWORD)
                    }
                })

            try {
                //Creating MimeMessage object
                val mm = MimeMessage(session)
                val emailId =useremailid.toString()
                //Setting sender address
                mm.setFrom(InternetAddress("grobazar12@gmail.com"))
                //Adding receiver
                mm.addRecipient(javax.mail.Message.RecipientType.TO,
                    InternetAddress(emailId))
                //Adding subject
                mm.addRecipient(javax.mail.Message.RecipientType.TO,
                    InternetAddress("grobazar12@gmail.com"))

                mm.subject =order!!.restuarnatname+" "+ timestamp
                //Adding message
                mm.setText("Mail Received .")

                var messagebody=MimeBodyPart()
                messagebody.setText("bill for ", timestamp)



                var messagebodypart=MimeBodyPart()
               messagebodypart.setText("Bill of order no "+timestamp)
                messagebodypart.setContent("Email sent","pdf")
                messagebodypart.attachFile(this.getExternalFilesDir(null).toString() + "/" + timestamp+".pdf")


                var multipart=MimeMultipart()
//                multipart.addBodyPart(messagebody)
                multipart.addBodyPart(messagebodypart)




                mm.setContent(multipart)


                //Sending email
                Transport.send(mm)



            } catch (e: MessagingException) {

                e.printStackTrace()
            }
        }
    }


private fun loaditems(timestamp: String)
{
    var myref=FirebaseDatabase.getInstance().getReference("Restaurant")
    myref.child("01").child("Requests").child(timestamp).addValueEventListener(object:ValueEventListener
    {


        override fun onCancelled(p0: DatabaseError) {
            Toast.makeText(this@Bill,"Something went wrong",Toast.LENGTH_LONG).show()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onDataChange(dataSnapshot: DataSnapshot) {

            var orderdetail=dataSnapshot.getValue(Orderdetail::class.java)


            restname.text=orderdetail!!.restuarnatname

            billquantitytotal.text=orderdetail!!.itemtotal
            billtotalamount.text="₹ "+orderdetail!!.total

            subtotaltext.text="₹ "+orderdetail!!.total
            totaltext.text="₹ "+orderdetail!!.total

                var currency=EnglishNumberToWords()

            totalwords.text=StringCapital.capital(currency.convert(orderdetail.total!!.toLong()))
            datebill.text="Date: "+orderdetail.date

            invoiceno.text="Invoice No: "+timestamp



        }
    })

    myref.child("01").child("Requests").child(timestamp).child("foods").addValueEventListener(object:ValueEventListener
    {

        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {

            for (snapshot in dataSnapshot.children)
            {
                var food=snapshot.getValue(Food::class.java)

                if(food!=null)
                {
                    foodList.add(food)
                    billAdapter.notifyDataSetChanged()

                }

            }



        }
    })






}

    private fun pdf(timestamp: String) {


        if (order != null) {



            val page =
                object : AbstractViewRenderer(this, R.layout.activity_bill) {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun initView(view: View?) {


                        view!!.findViewById<TextView>(R.id.billquantitytotal).text =
                            order!!.itemtotal



                        view!!.findViewById<TextView>(R.id.billtotalamount).text =  "₹ "+order!!.total
                        view!!.findViewById<TextView>(R.id.subtotaltext).text = "₹ "+ order!!.total
                        view!!.findViewById<TextView>(R.id.totaltext).text = "₹ "+order!!.total
                        view!!.findViewById<TextView>(R.id.restname).text =  order!!.restuarnatname
                        view!!.findViewById<TextView>(R.id.datebill).text = "Date: " + order!!.date
                        view!!.findViewById<TextView>(R.id.invoiceno).text = "Invoice No: " + order!!.timestamp

                        view!!.findViewById<RecyclerView>(R.id.billfood).adapter =
                            BillAdapter(foodlistview, this@Bill)
                        var capital=StringCapital

                        view!!.findViewById<TextView>(R.id.totalwords).text = capital.capital(EnglishNumberToWords().convert(order!!.total!!.toLong()))

                        view!!.findViewById<RecyclerView>(R.id.billfood).layoutManager =
                            LinearLayoutManager(this@Bill)


                    }

                    private var _text: String? = null

                    fun setText(text: String) {
                        _text = text
                    }

                }

// you can reuse the bitmap if you want
            page.isReuseBitmap = true


            val doc = PdfDocument(this)

// add as many pages as you have
            doc.addPage(page)

            var display = getWindowManager().getDefaultDisplay();
var size =  Point();
display.getSize(size);
var width = size.x;
var height = size.y;
            doc.setRenderWidth(1000)
            doc.setRenderHeight(height)
            doc.setOrientation(PdfDocument.A4_MODE.PORTRAIT)
            doc.setFileName(timestamp)
            doc.setSaveDirectory(this.getExternalFilesDir(null))

            doc.setInflateOnMainThread(false)
            doc.setListener(object : PdfDocument.Callback {
                override fun onComplete(file: File) {

                    println("pdf created")
                    sendEmail(timestamp)

                    //     getpdf(timestamp)
                }

                override fun onError(e: Exception) {

                    println("eroor takes place")
                }
            })

            doc.createPdf(this)

            //  getpdf()

        }
    }


    override fun onBackPressed() {
        super.onBackPressed()


        startActivity(Intent(this,Bottom_Navigation::class.java))


    }



}
