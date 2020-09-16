package com.grobazar.grobazarapp.Adpaters
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grobazar.grobazarapp.Activities.SubCategory
import com.grobazar.grobazarapp.Category
import com.grobazar.grobazarapp.R
import com.squareup.picasso.Picasso


class CategoryAdapter(val imagelist: ArrayList<Category>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(imagelist[position])

    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.categorylayout, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return imagelist.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(category: Category) {

            itemView.findViewById<TextView>(R.id.categoryname).text=category.name
            val image=itemView.findViewById<ImageView>(R.id.cateogoryimage)
                Picasso.get().load(category.image).into(image)


            itemView.setOnClickListener{

                var context=itemView.context
               var intent= Intent(context,SubCategory::class.java)
                intent.putExtra("key",category.key)

                intent.putExtra("category",category.name)
                context.startActivity(intent)
            }




        }
    }
}