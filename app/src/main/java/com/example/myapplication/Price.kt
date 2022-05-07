package com.example.myapplication

interface Price {
    fun get_price(type:Int): Int {
        var res:Int = 0
        when(type){
            2-> res=50 //Prix Attack tower
            3-> res=250// Prix Money tower
            4-> res=250 // Prix Ice Tower
            5-> res=30 // Prix sacrifice tower
        }
        return res
    }
}