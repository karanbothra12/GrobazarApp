package com.grobazar.grobazarapp.ui.Cart

import android.app.Dialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.grobazar.grobazarapp.Activities.Documentation
import com.grobazar.grobazarapp.Activities.MainActivity
import com.grobazar.grobazarapp.Adpaters.PastOrderAdapter
import com.grobazar.grobazarapp.PastOrdersDetail
import com.grobazar.grobazarapp.R
import com.grobazar.grobazarapp.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.messaging.RemoteMessage
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment() {

    private lateinit var dashboardViewModel: ProfileViewModel

    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference
    private lateinit var PastOrderAdapter:PastOrderAdapter
    private  var pastOrderlist:ArrayList<PastOrdersDetail>?=null
    private lateinit var datause:Users
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)


        var useraddress:String?=null

         pastOrderlist=ArrayList<PastOrdersDetail>()
//        pastOrdersDetaillist.add(PastOrdersDetail())
//        pastOrdersDetaillist.add(PastOrdersDetail())
//        pastOrdersDetaillist.add(PastOrdersDetail())
//        pastOrdersDetaillist.add(PastOrdersDetail())
//        pastOrdersDetaillist.add(PastOrdersDetail())



        PastOrderAdapter = PastOrderAdapter(pastOrderlist!!)

        root.pastorders.adapter = PastOrderAdapter
       var layoutmanage= LinearLayoutManager(context)
        layoutmanage.reverseLayout=true
        root.pastorders.layoutManager =layoutmanage
        root.Logout.setOnClickListener {
            var mAuth:FirebaseAuth=FirebaseAuth.getInstance()
            mAuth.signOut()


            startActivity(Intent(context,MainActivity::class.java))

        }

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


                if(user!!.email!=null) {

                    datause=user!!

                    root.useremail.text = user!!.email.toString()
                    root.username.text = user!!.name.toString()
                    root.userphone.text = user!!.phoneno.toString()
                    useraddress=user!!.address.toString()
                    println("user adress is fetched and it is "+useraddress)

                }


            }



        })

        root.help.setOnClickListener {


            var intent=Intent(context,Documentation::class.java)
            startActivity(intent)
        }

        var orderref=database.getReference("Restaurant")
try {


    orderref.child("01").child("Requests").addValueEventListener(object : ValueEventListener {


        override fun onCancelled(p0: DatabaseError) {

            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
        }

        override fun onDataChange(datasnapshot: DataSnapshot) {

            println("in method on data chnage"+datasnapshot.value)
            for (snapshot in datasnapshot.children) {
                var order = snapshot.getValue(PastOrdersDetail::class.java)



                println(order!!.phone)
                if (order!!.phone == FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString().removePrefix("+91"))
                {



                    pastOrderlist!!.add(order)




                    println(order.date)


                    root.pastorders.adapter!!.notifyDataSetChanged()
                }
                else
                {
                    println("No order found")
                }
            }


        }
    })
}catch (e:Exception)
{
    Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show()
    println(" Eroor is  mlksdal;s"+e.toString())
}
        if(pastOrderlist!!.size>0)
        {
            root.pastordertext.visibility=View.VISIBLE
        }

        if(pastOrderlist!=null) {


        }





        var dialog=Dialog(context!!)

        dialog.setContentView(R.layout.addressmanage)
        dialog.setCancelable(false)

        var address=dialog.findViewById<EditText>(R.id.addresschange)
        var restname=dialog.findViewById<EditText>(R.id.RestaurantNamechange)
        var persname=dialog.findViewById<EditText>(R.id.personnamechnage)
        var emailchange=dialog.findViewById<EditText>(R.id.emailchange)


        var database=FirebaseDatabase.getInstance()
        var ref=database.getReference("User")





        var done=dialog.findViewById<ImageButton>(R.id.doneaddr)
        dialog.setCancelable(true)

        done.setOnClickListener {
try {
    datause!!.address=address.text.toString()


    datause!!.restaurant_Name=restname.text.toString()


    datause!!.name=persname.text.toString()
    datause!!.email=emailchange.text.toString()

try {


    updateaddress()
    dialog.dismiss()
}catch (e:Exception)
{
    println(e.toString())
}

    Toast.makeText(context,"Address chnaged Sucessfully",Toast.LENGTH_LONG).show()
    if(view!!.isShown) {


        //dialog.dismiss()
        println(dialog.isShowing)


    }


}catch (e:Exception)
{
    println(" Eroor is "+e.toString())

}




        }

       root.manageaddres.setOnClickListener {


           if(useraddress!=null) {
               var add = useraddress
               println(add)
               address.setText("" + add)

               restname.setText(""+user!!.restaurant_Name)

               persname.setText(""+user!!.name)
               emailchange.setText(""+user!!.email)

           }
           if(view!!.isShown) {


               dialog.show()

           }



        }





        return root
    }

private fun updateaddress()


{


    var ref=FirebaseDatabase.getInstance().getReference("User")
    ref.child(FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString().removePrefix("+91"))
        .setValue(datause)
}

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        val title = data["title"]
        val message = data["message"]
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(
            RingtoneManager.TYPE_NOTIFICATION
        )
        val builder =
            NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val noti =
            context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        noti.notify(0, builder.build())
    }

}