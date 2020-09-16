package com.grobazar.grobazarapp.Common


import com.google.firebase.database.FirebaseDatabase

class DatabaseCommon {


    var database = FirebaseDatabase.getInstance()

    var users = database.getReference("User")
    var requests = database.getReference("Requests")
    var foods = database.getReference("Foods")
    var ratingTbl = database.getReference("Rating")
    var foodList = database.getReference("Foods")
    var category = database.getReference("Restaurant")
    var restaurant = database.getReference("Restaurant")

    var banners = database.getReference("Banner")
    var tokens = database.getReference("Tokens")
    var table_user = database.getReference("User")
    var shippingOrder = database.getReference("ShippingOrders")
    //   public DatabaseReference restaurants = database.getReference("Restaurants");
    var adminbanners = database.getReference("AdminBanner")
    var subtreandingProduct = database.getReference("SubTreandingProduct")
    var documentation = database.getReference("Documentation")

}
