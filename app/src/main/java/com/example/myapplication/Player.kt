package com.example.myapplication

class Player {
    var money = 100
    var healthpoints = 3
    var gameover = false

    fun afford (price:Int): Boolean {
        var res = false
        if (money-price >=0){
            res = true
            this.money-=price
        }
        return res
    }

    fun lose_healthpoints(){
        healthpoints-=1
        if(healthpoints<=0){gameover = true}
    }

    fun reset(){
        money = 100
        healthpoints = 3
    }
}