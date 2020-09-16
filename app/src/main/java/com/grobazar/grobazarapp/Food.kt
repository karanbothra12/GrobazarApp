package com.grobazar.grobazarapp

import java.io.Serializable

class Food():Serializable
{

        var name: String? = null
        var image: String? = null
        var description: String? = null
        var price: Int ? =null
        var discount: Int ?=null
        var reserve:Int?=null
        var storeNo:String?=null
        var visibility: String? = null

        var menulimit: Int? =null

        var category:String?=null

        var subCategory:String?=null
         var MenuID: String? = null
         var MenuID_Number: String? = null


        var cart:String?="1"
        var cartitem:Int=0
}

