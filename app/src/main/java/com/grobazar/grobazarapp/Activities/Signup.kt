package com.grobazar.grobazarapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.grobazar.grobazarapp.R
import com.grobazar.grobazarapp.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*
import java.lang.Exception

class Signup : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        mAuth = FirebaseAuth.getInstance()

        signupforward.setOnClickListener{
            var user=Users()



//            if(personname.text.toString()!=null!!||PhoneNumber.text.toString()!=null||RestaurantName.text.toString()!=null||email.text.toString()!=null||personname.Adresss.toString()!=null)
//            {
//
//            }else
//            {
//                Toast.makeText(this,"Please Fill out all details",Toast.LENGTH_LONG).show()
//            }


            user.phoneno=PhoneNumber.text.toString()
            user.name=personname.text.toString()
            user.restaurant_Name=RestaurantName.text.toString()
            user.email=email.text.toString()
            user.address=Adresss.text.toString()
            user.uid=mAuth.uid.toString()


            if( user.phoneno.equals("")||user.name.equals("")||user.restaurant_Name.equals("")||user.email.equals("")||user.address.equals(""))
            {
                Toast.makeText(this,"Please fillout all details",Toast.LENGTH_LONG).show()

            }
else
            {
                signup(FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString().removePrefix("+91"),user)

            }







        }
    }
    private fun signup(phoneno:String,user:Users)
    {

        try {


            var database: FirebaseDatabase = FirebaseDatabase.getInstance()

            var myref = database.getReference("User")



             myref.child(phoneno).setValue(user)
             println("Signup Running ")

            var intent=Intent(this, Bottom_Navigation::class.java)
            intent.putExtra("phone",phoneno)
            startActivity(intent)

            finish()


        }
        catch (e:Exception)
        {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show()
            println("Error is " +  e)



        }




    }





}
