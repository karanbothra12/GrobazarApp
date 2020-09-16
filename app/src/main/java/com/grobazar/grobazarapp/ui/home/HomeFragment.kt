package com.grobazar.grobazarapp.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.grobazar.grobazarapp.*
import com.grobazar.grobazarapp.Adpaters.BannerAdapter
import com.grobazar.grobazarapp.Adpaters.CategoryAdapter
import com.grobazar.grobazarapp.Adpaters.TrendingAdapter
import com.grobazar.grobazarapp.Common.DatabaseCommon
import com.grobazar.grobazarapp.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.view.bannrerecycler
import kotlinx.android.synthetic.main.activity_home.view.trendingrecycler
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    var bannerAdapter: BannerAdapter? = null
    var bannerlayout: LinearLayoutManager? = null
    var cateogryAdapter: CategoryAdapter? = null
    var categorylayout: GridLayoutManager? = null
    var trendingAdapter: TrendingAdapter? = null
    var trendinglayout: LinearLayoutManager? = null
    internal var databaseCommon = DatabaseCommon()
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference
    private lateinit var bannerref: DatabaseReference
    private lateinit var banne:HashMap<String,String>

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel.text.observe(this, Observer {
        })

        banne=HashMap<String,String>()
        var imageliist = ArrayList<String>()
        imageliist.add("grocery1")
        var trendlist = ArrayList<Food>()

        var food = Food()
//        val gifImageView = root.findViewById(R.id.GifImageView) as GifImageView
//        gifImageView.setGifImageResource(R.drawable.grogif)

        loadtrending()
        database= FirebaseDatabase.getInstance()
        myRef=database.getReference("Restaurant")

        trendingAdapter = TrendingAdapter(trendlist)

        trendinglayout = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        root.trendingrecycler.adapter = trendingAdapter
        root.trendingrecycler.layoutManager = trendinglayout
        root.trendingrecycler.addItemDecoration(ItemDecoration(20))

        myRef.child("01").child("detail").child("SubTreandingProduct").addValueEventListener(object :ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context,"Something went wrong ",Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                println("on data chnage is called")

                root.grogif.visibility=View.INVISIBLE
                for (snapshot in dataSnapshot.children)
                {
                    var trend=snapshot.getValue(Food::class.java)

                    if(trend!=null)
                    {

                        trendlist.add(trend)
                        root.trendingrecycler.adapter!!.notifyDataSetChanged()



                    }



                }



            }
        })

        var bannerlist=ArrayList<Banner>()

        var a=0
    var   ha= Handler()
    ha.postDelayed(object :Runnable {

    override  fun run() {

    a=changeimage(a)

    if(a==bannerlist.size)
    {
        a=0
    }
        ha.postDelayed(this, 5000);

    }
}, 5000);

        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("Restaurant")

        bannerAdapter = BannerAdapter(bannerlist)
        bannerlayout = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        root.bannrerecycler.adapter = bannerAdapter
        root.bannrerecycler.layoutManager = bannerlayout
        root.bannrerecycler.adapter!!.notifyDataSetChanged()

        myRef.child("01").child("detail").child("Banner").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                println("erooro takes")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {


                for (snapshot in dataSnapshot.children)
                {
                    var banner=snapshot.getValue(Banner::class.java)



                    var bannerobj=Banner()
                    bannerobj.name=banner!!.name
                    bannerobj.image=banner!!.image
                    bannerobj.id=banner!!.id

                        bannerlist!!.add(bannerobj)
                        root.bannrerecycler.adapter!!.notifyDataSetChanged()






                }
            }
        })






        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("Restaurant")
        var categorylist1=ArrayList<Category>()

        var ref=myRef.child("01").child("detail").child("Category")

        ref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                println("erooro takes")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {


                for (snapshot in dataSnapshot.children)
                {


                   var category=snapshot.getValue(Category::class.java)

                    var categoryobject=Category()

                    if(category!=null)
                    {
                        categoryobject.name=category.name
                        categoryobject.image=category.image
                        categoryobject.subCategory=category.subCategory
                        categoryobject.key=snapshot.key
                        categorylist1!!.add(categoryobject)
                        root.fragcateogoryreccyler.adapter!!.notifyDataSetChanged()


                    }



                }
            }
        })



        cateogryAdapter = CategoryAdapter(categorylist1!!)
        categorylayout = GridLayoutManager(context, 2)




       root.fragcateogoryreccyler.adapter=cateogryAdapter
        root.fragcateogoryreccyler.layoutManager=GridLayoutManager(context,2)

        root.fragcateogoryreccyler.isNestedScrollingEnabled=false
        root.fragcateogoryreccyler.addItemDecoration(ItemDecoration(20))


        return root
    }




    fun changeimage(i:Int):Int
    {
        if(bannerAdapter!=null)
        {

    if(bannrerecycler!=null) {

        bannrerecycler.smoothScrollToPosition(i)

    }
        }


        var a=i+1


        return a
    }


    override fun onStart() {
        super.onStart()
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("Restaurant")









    }


    private fun loadtrending()
    {




    }



}
