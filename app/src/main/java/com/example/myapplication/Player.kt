package com.example.myapplication

class Player() {
    var money = 130 // Argent du joueur
    var healthpoints = 3 // points de vie totals du joueur
    var gameover = false
    var score = 0 // Score de la partie

    fun afford (price:Int): Boolean {
        //Teste si le joueur peut acheter l'objet sélectionné
        var res = false
        if (money-price >=0){
            res = true
        }
        return res
    }

    fun lose_healthpoints(){
        //Retire un point de vie lorsque un monstre arrive au bout du chemin
        healthpoints-=1
        if(healthpoints<=0){gameover = true}
    }

    fun reset(){
        //Lors d'une nouvelle partie, ces paramètres sont réinitialisés
        money = 130
        healthpoints = 3
        score = 0
        gameover = false
    }
}