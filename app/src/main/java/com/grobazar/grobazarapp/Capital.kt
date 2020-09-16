package com.grobazar.grobazarapp

object StringCapital {
    @JvmStatic
 public fun capital(str:String):String {

        val result = StringBuilder(str.length)
        val words = str.split("\\ ").toTypedArray()
        for (i in words.indices) {
            result.append(Character.toUpperCase(words[i][0]))
                .append(words[i].substring(1)).append(" ")
        }


        return result.toString()
    }


}