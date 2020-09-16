package com.grobazar.grobazarapp.Activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.grobazar.grobazarapp.Adpaters.BannerAdapter
import com.grobazar.grobazarapp.Adpaters.CategoryAdapter
import com.grobazar.grobazarapp.Adpaters.TrendingAdapter
import com.grobazar.grobazarapp.Category
import com.grobazar.grobazarapp.R
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {

    var bannerAdapter: BannerAdapter?=null
    var bannerlayout:LinearLayoutManager?=null
    var cateogryAdapter: CategoryAdapter?=null
    var categorylayout:GridLayoutManager?=null
    var trendingAdapter: TrendingAdapter?=null
    var trendinglayout:LinearLayoutManager?=null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var imageliist=ArrayList<String>()
        imageliist.add("grocery1")
        setSupportActionBar(toolbar as Toolbar?)
//        setTitle("Gro Bazar")

        var action=supportActionBar
        action!!.setDisplayShowTitleEnabled(false)
//        action!!.setLogo(R.drawable.gro)
        var categorylist=ArrayList<Category>()

//                var category1=Category()
//            category1.image="https://firebasestorage.googleapis.com/v0/b/grobazar-ac888.appspot.com/o/images%2FCategory%20Image%2Fgrocery%20icon.jpg?alt=media&token=331d8a95-e5ad-498b-b78e-2dbb81fb9e5c"
//            category1.name="Grocery And Staples"
//        var category2=Category()
//        category2.image="https://firebasestorage.googleapis.com/v0/b/grobazar-ac888.appspot.com/o/images%2Fa1fd5693-21d7-471a-955c-445055a0ae42?alt=media&token=928a14dc-40ab-42cb-88b8-464ffcd3c2f9"
//        category2.name="Household Items"
//        var category3=Category()
//        category3.image="https://firebasestorage.googleapis.com/v0/b/grobazar-ac888.appspot.com/o/images%2Ff5142af9-ebc6-44af-9049-b6a76d9ae28d?alt=media&token=8c7a7116-3685-45e0-bc40-3ae7ff37572f"
//        category3.name="Beverages"
//
//
//        var category4=Category()
//        category4.image="https://firebasestorage.googleapis.com/v0/b/grobazar-ac888.appspot.com/o/images%2Fd17fafe1-f3f4-44a4-9182-df165f00baa2?alt=media&token=0f9b54d2-6eed-4d6f-8c3c-d9cb49b9aee8"
//        category4.name="Buiscuits Snacks and Choclates"
//
//        var category5=Category()
//        category5.image="https://firebasestorage.googleapis.com/v0/b/grobazar-ac888.appspot.com/o/images%2Ff2b656f5-5e81-4207-9cb5-cd161ac4928f?alt=media&token=3437fe00-4b0b-4524-94bd-85fb23ff87fd"
//        category5.name="Noodles Sauces and Insatant Food"
//
//        var category6=Category()
//        category6.image="https://firebasestorage.googleapis.com/v0/b/grobazar-ac888.appspot.com/o/images%2Ff3291d65-878d-49af-a071-fb317e8ba20f?alt=media&token=ed4bb023-70fc-45c0-a485-00f5a9ab0dfc"
//        category6.name="Personal Care"
//
//
//
//        categorylist.add(category1)
//
//        categorylist.add(category2)
//        categorylist.add(category3)
//        categorylist.add(category4)
//        categorylist.add(category5)
//        categorylist.add(category6)
//
//
//
//
//        bannerAdapter= BannerAdapter(imageliist)
//         bannerlayout=LinearLayoutManager(this,LinearLayout.HORIZONTAL,false)
//         cateogryAdapter= CategoryAdapter(categorylist)
//         categorylayout= GridLayoutManager(this,2)
//
//        bannrerecycler.adapter=bannerAdapter
//        bannrerecycler.layoutManager=bannerlayout
//
//        cateogoryreccyler.adapter=cateogryAdapter
//        cateogoryreccyler.layoutManager=categorylayout
//
//        cateogoryreccyler.addItemDecoration(ItemDecoration(40))



    }
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.home,menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
//        R.id.toolbar -> {
//            // User chose the "Print" item
//            Toast.makeText(this,"Print action",Toast.LENGTH_LONG).show()
//            true
//        }
//        android.R.id.home ->{
//            Toast.makeText(this,"Home action",Toast.LENGTH_LONG).show()
//            true
//        }
//
//        else -> {
//            // If we got here, the user's action was not recognized.
//            // Invoke the superclass to handle it.
//            super.onOptionsItemSelected(item)
//        }
//    }
//
//

}
