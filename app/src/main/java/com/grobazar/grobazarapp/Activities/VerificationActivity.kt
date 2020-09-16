package com.grobazar.grobazarapp.Activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.grobazar.grobazarapp.R
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_verification.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class VerificationActivity : AppCompatActivity() {


    lateinit var database:FirebaseDatabase
    lateinit var myRef:DatabaseReference


    private var phonenumber:String?=null
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
        lateinit var mAuth: FirebaseAuth
        var verificationId = ""


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)


            try {






                setContentView(R.layout.activity_verification)
                termslink.setOnClickListener {

                    startActivity(Intent(this,Documentation::class.java))
                }


                mAuth = FirebaseAuth.getInstance()

                termscond.setOnClickListener {
                 if(termscond.isChecked) {
                     verficationbutton.visibility = View.VISIBLE
                 }
                    else
                 {
                     verficationbutton.visibility = View.INVISIBLE

                 }
                }
                if(termscond.isChecked)
                {

                    verficationbutton.visibility=View.VISIBLE
                }
                else
                {
                    verficationbutton.visibility=View.INVISIBLE

                }
                verficationbutton.setOnClickListener {
                    //          view: View? -> progress.visibility = View.VISIBLE


                    termscond.visibility=View.INVISIBLE
                    termslink.visibility=View.INVISIBLE
                    if (phone.text == null || phone.text.length != 10) {



                        toast("Please enter valid number")
                        termscond.visibility=View.VISIBLE
                        termslink.visibility=View.VISIBLE


                    } else {

                        phonenumber=phone.text.toString()
                        constraintLayout.setBackgroundResource(0)
                        verificationbackground.setBackgroundResource(R.drawable.gradient)
                        phone.visibility = View.INVISIBLE
                        verficationbutton.visibility = View.INVISIBLE
                        phonetext.visibility = View.INVISIBLE

                        Verify.visibility = View.VISIBLE
                        verificationcode.visibility = View.VISIBLE
                                           verify()
                    }
                }

                Verify.setOnClickListener {
                    //       view: View? -> progress.visibility = View.VISIBLE

                    Verify.isEnabled=false
                    if(verificationcode.text!=null) {

                        authenticate()

                    }





                }
            }catch (e:Exception)
            {
                toast("Some error occurs try again")
                println(e)
            }
        }

        private fun verificationCallbacks() {
            mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    //  progress.visibility = View.INVISIBLE

                    var code:String=credential.smsCode.toString()

                    println(code +" otp code is ")

                    if(code.equals("null"))
                    {

                    }
                    else {
                        verificationcode.setText(code)
                    }

                    signIn(credential)

                }

                override fun onVerificationFailed(p0: FirebaseException?) {

                    Toast.makeText(this@VerificationActivity,p0.toString(),Toast.LENGTH_LONG).show()

                 }

                override fun onCodeSent(
                    verfication: String?,
                    p1: PhoneAuthProvider.ForceResendingToken?
                ) {
                    super.onCodeSent(verfication, p1)
                    verificationId = verfication.toString()
                    //    progress.visibility = View.INVISIBLE




                }

            }
        }

        private fun verify() {

            verificationCallbacks()

            val phnNo = "+91"+phone.text.toString()

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phnNo,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
            )
        }

        private fun signIn(credential: PhoneAuthCredential) {
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        check.check()
                        Handler().postDelayed({
                            //Do something after 100ms
                        }, 1000)


                        Verify.isEnabled=false
                        nextactivity()
                    }
                    else
                    {
                        Toast.makeText(this,"Inavlid code ",Toast.LENGTH_LONG).show()


                        Verify.isEnabled=true
                    }
                }
        }

        private fun authenticate() {

            val verifiNo =  verificationcode.text.toString()

            try {


                val credential: PhoneAuthCredential =
                    PhoneAuthProvider.getCredential(verificationId, verifiNo)

                signIn(credential)
            }catch (e:Exception)

            {
                toast("Wrong code")
                Verify.isEnabled=true

            }

        }

        private fun toast(msg: String) {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }

        override fun onBackPressed() {

            var intent = Intent()
            intent.putExtra("status", "sucessfull")
            setResult(Activity.RESULT_OK, intent)
            finish();


            super.onBackPressed()
        }

 fun nextactivity()
 {
     database = FirebaseDatabase.getInstance()
     myRef = database.getReference("User")



     var ref=myRef


     ref.addValueEventListener(object : ValueEventListener {
         override fun onCancelled(p0: DatabaseError) {
             println("erooro takes")
         }

         override fun onDataChange(dataSnapshot: DataSnapshot) {


             if(dataSnapshot.hasChild(phonenumber!!))
             {

                 var intent=Intent(this@VerificationActivity,Bottom_Navigation::class.java)
                 intent.putExtra("phonenumber",phonenumber)
                 if(termscond.isChecked) {
                     toast("Logged in Successfully :)")

                     startActivity(intent)
                     finish()

                 }
                 else
                 {
                     Toast.makeText(this@VerificationActivity,"Please accept our terms and conditions",Toast.LENGTH_LONG).show()
                 }
             }
             else
             {
                 println("user didnot exists                      new user will be created")
                 var intent=Intent(this@VerificationActivity,Signup::class.java)
                 intent.putExtra("phonenumber",phonenumber)
                     toast("Logged in Successfully :)")

                     startActivity(intent)
                     finish()


             }

         }
     })

 }






    }

