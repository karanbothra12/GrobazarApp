package com.grobazar.grobazarapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.grobazar.grobazarapp.Adpaters.SubCategoryAdapter
import com.grobazar.grobazarapp.ItemDecoration
import com.grobazar.grobazarapp.R
import com.grobazar.grobazarapp.subCategory
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sub_category.*

class SubCategory : AppCompatActivity() {

private lateinit var database:FirebaseDatabase
   private  lateinit  var  myRef:DatabaseReference
        var  subCategoryAdapter:SubCategoryAdapter?=null
    var subcategorylayout: GridLayoutManager?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)
        setSupportActionBar(toolbarsub)
//        setTitle("Gro Bazar")

        var action = supportActionBar
        action!!.setDisplayShowTitleEnabled(false)
        action!!.setDisplayHomeAsUpEnabled(true)
//        action!!.setLogo(R.drawable.gro)
        var subcategorylist = ArrayList<subCategory>()

        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("Restaurant")
        var key=""
        var catname:String?=null
        if (intent.extras != null)
        {
            key=intent.getStringExtra("key")
            catname=intent.getStringExtra("category")
            println("category name in subcategory is "+catname)
        }
        println(key)
        var ref=myRef.child("01").child("detail").child("Category").child(key).child("subCategory")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println("erooro takes")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {


                for (snapshot in dataSnapshot.children)
                {
                    var category=snapshot.getValue(subCategory::class.java)

                    var categoryobject=subCategory()

                    if(category!=null)
                    {
                        categoryobject.name=category.name
                        categoryobject.image=category.image
                        categoryobject.categoryname=catname
                        categoryobject.key=snapshot.key
                        subcategorylist!!.add(categoryobject)
                        subcategoryrecycler.adapter!!.notifyDataSetChanged()

                    }



                }
            }
        })
    subCategoryAdapter= SubCategoryAdapter(subcategorylist)
        subcategorylayout= GridLayoutManager(this,3)


        subcategoryrecycler.adapter=subCategoryAdapter
        subcategoryrecycler.layoutManager=subcategorylayout

        subcategoryrecycler.addItemDecoration(ItemDecoration(30))


    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }
}
