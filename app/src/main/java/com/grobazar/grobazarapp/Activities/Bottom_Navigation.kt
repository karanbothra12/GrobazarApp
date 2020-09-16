package com.grobazar.grobazarapp.Activities

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.grobazar.grobazarapp.Common.Cart
import com.grobazar.grobazarapp.R
import java.lang.Exception

class Bottom_Navigation : AppCompatActivity() {
    var cart= Cart()
    var phone:String=""


        override fun onCreate(savedInstanceState: Bundle?) {
            try {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_bottom__navigation)
                val navView: BottomNavigationView = findViewById(R.id.nav_view)

                val navController = findNavController(R.id.nav_host_fragment)
                // Passing each menu ID as a set of Ids because each
                // menu should be considered as top level destinations.
                val appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.navigation_home,
                        R.id.navigation_search,
                        R.id.navigation_cart
                    )
                )


                navView.setupWithNavController(navController)
                if (intent != null) {
                    if (intent.getStringExtra("phone") != null) {
                        phone = intent.getStringExtra("phone")

                    }
                }


            } catch (e: Exception) {
                println(" Eroor is  mlksdal;s" + e.toString())

            }
        }


}
