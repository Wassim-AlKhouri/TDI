package com.example.myapplication

class Player() {
    var money = 1000
    var healthpoints = 2
    var gameover = false
    var score = 0

    fun afford (price:Int): Boolean {
        // fonction qui teste si le joueur a assez d'argent pour faire un achat
        var res = false
        if (money-price >=0){
            res = true
        }
        return res
    }

    fun lose_healthpoints(){
        healthpoints-=1
        if(healthpoints<=0){gameover = true}
    }

    fun reset(){
        // reinitialise les attribues du joueur
        money = 150
        healthpoints = 3
        score = 0
        gameover = false
    }
}