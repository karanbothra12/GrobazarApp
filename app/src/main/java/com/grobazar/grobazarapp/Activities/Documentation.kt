package com.grobazar.grobazarapp.Activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grobazar.grobazarapp.R
import java.lang.Exception

class Documentation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        try {


            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_documentation)
            val btnAbout =
                findViewById<View>(R.id.btnAbout) as Button
            val btnpp =
                findViewById<View>(R.id.btnpp) as Button
            val btntc =
                findViewById<View>(R.id.btntc) as Button
            val btnph =
                findViewById<View>(R.id.btnphone) as Button
            val btnem =
                findViewById<View>(R.id.btnEmail) as Button
            btnAbout.setOnClickListener {
                // Setting Dialog Title
                val uri =
                    Uri.parse("https://drive.google.com/open?id=1fJtQ_SwgU3huqW9Wp8d5wutO6G6-b3wn") // missing 'http://' will cause crashed
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
            btnpp.setOnClickListener {
                // Setting Dialog Title
                val uri =
                    Uri.parse("https://drive.google.com/open?id=1gPPjlee_91GfS264kkaFk1jdE5UcDPyz") // missing 'http://' will cause crashed
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
            btntc.setOnClickListener {
                val uri =
                    Uri.parse("https://drive.google.com/open?id=1xrNINgjHj2wglP3ycxYJCu7dd5D647fo") // missing 'http://' will cause crashed
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
            btnph.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:+918946999829")
                startActivity(intent)
            }
            btnem.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "message/rfc822"
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("grobazar12@gmail.com"))
                intent.putExtra(Intent.EXTRA_SUBJECT, "Customer Care")
                try {
                    startActivity(Intent.createChooser(intent, "Send mail"))
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        this@Documentation,
                        "There are no email clients installed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }catch (e:Exception)
        {
            e.printStackTrace()
        }
        }

}
