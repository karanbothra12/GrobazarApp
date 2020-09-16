package com.grobazar.grobazarapp.ui.Search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.grobazar.grobazarapp.Adpaters.SearchFoodAdapter
import com.grobazar.grobazarapp.Food
import com.grobazar.grobazarapp.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_search.view.*


class SearchFragment : Fragment() {

    private lateinit var notificationsViewModel: SearchViewModel
private lateinit var database:FirebaseDatabase
    private lateinit var ref:DatabaseReference
    private lateinit var searchFoodAdapter: SearchFoodAdapter
    private lateinit var searchhlayout:LinearLayoutManager
    private  var foodresult=ArrayList<Food>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        notificationsViewModel =
            ViewModelProviders.of(this).get(SearchViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_search, container, false)

        searchhlayout= LinearLayoutManager(context)
        searchFoodAdapter=SearchFoodAdapter(foodresult,requireContext())

        root.results.adapter=searchFoodAdapter
        root.results.layoutManager=searchhlayout
        database=FirebaseDatabase.getInstance()
            ref=database.getReference("Restaurant")



        root.searchView.queryHint="Serach item.."
        root.searchView.setOnClickListener {
            root.searchView.isIconified=false
        }

        root.searchView.onActionViewExpanded();
       root. searchView.setIconifiedByDefault(false);

        if(!root.searchView.isFocused()) {
            root.searchView.clearFocus();
        }


 root.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                var firebaseQuery= ref.child("01").child("detail").child("Foods").orderByChild("name").startAt(query)

                firebaseQuery.addValueEventListener(object:ValueEventListener
                {
                    override fun onCancelled(p0: DatabaseError) {


                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {


                        foodresult.clear()

                        for(snapshot in dataSnapshot.children)
                        {

                            var food=snapshot.getValue(Food::class.java)



                            if(food!=null) {

                                foodresult.add(food)
                                root.results.adapter!!.notifyDataSetChanged()

                            }

                        }




                    }
                })
                return true


            }

            override fun onQueryTextChange(newText: String?): Boolean {

                var  firebaseQuery= ref.child("01").child("detail").child("Foods").orderByChild("name").startAt(newText)

                root.searchpress.visibility=View.GONE
                firebaseQuery.addValueEventListener(object:ValueEventListener
                {
                    override fun onCancelled(p0: DatabaseError) {


                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        foodresult.clear()
                        for(snapshot in dataSnapshot.children)
                        {

                            var food=snapshot.getValue(Food::class.java)



                            if(food!=null) {

                                foodresult.add(food)
                                root.results.adapter!!.notifyDataSetChanged()

                            }

                        }



                    }
                })

                return true

            }


        })






        return root


    }

    fun suggestions(query:String)
    {


    }

    fun resultsload(query:String)
    {
    }


}