package com.example.myapplication

interface Price {
    fun get_price(type:Int): Int {
        var res:Int = 0
        when(type){
            2-> res=50
            3-> res=250
            4-> res=250
            5-> res=30
        }
        return res
    }
}