package com.grobazar.grobazarapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.grobazar.grobazarapp.R
import java.lang.Exception
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {
    var Request_Code=1

    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        try {


            super.onCreate(savedInstanceState)

            setContentView(R.layout.activity_main)
            mAuth = FirebaseAuth.getInstance()

            val timer = object : Thread() {
                override fun run() {
                    try {
                        //Display for 3 seconds
                        sleep(3000)
                    } catch (e: InterruptedException) {

                        e.printStackTrace()
                    } finally {
                        if(mAuth.currentUser!=null)
                        {
                            //startActivity(Intent(this@MainActivity,Bottom_Navigation::class.java).putExtra("phone","200"))

                            nextactivity()
                        }
                        else
                        {
                            startActivity(Intent(this@MainActivity,VerificationActivity::class.java))
                            finish()
                        }


                    }
                }
            }
            timer.start()


        }catch (e:Exception)
        {
            println("Some error occurs try again")
        }



    }


    override fun onStart() {



        super.onStart()
    }

    fun nextactivity()
    {
      var  database = FirebaseDatabase.getInstance()
        var myRef = database.getReference("User")



        var ref=myRef


        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println("erooro takes")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {


                if(dataSnapshot.hasChild(mAuth.currentUser!!.phoneNumber.toString().removePrefix("+91")))
                {

                    var intent=Intent(this@MainActivity,Bottom_Navigation::class.java)
                    intent.putExtra("phonenumber",mAuth.currentUser!!.phoneNumber.toString().removePrefix("+91"))
                    startActivity(intent)

                    finish()
                }
                else
                {
                    println("user didnot exists                      new user will be created")
                    var intent=Intent(this@MainActivity,Signup::class.java)
                    intent.putExtra("phonenumber",mAuth.currentUser!!.phoneNumber.toString().removePrefix("+91"))
                    startActivity(intent)

                    finish()

                }

            }
        })

    }


}
