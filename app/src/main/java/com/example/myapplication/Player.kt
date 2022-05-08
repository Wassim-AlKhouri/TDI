package com.example.myapplication

class Player() {
    var money = 1000 // Argent du joueur
    var healthpoints = 2 // Vie totale du joueur
    var gameover = false
    var score = 0 // Score de la partie

    fun afford (price:Int): Boolean {
        //Test si le joueur peut acheter l'objet sélectionner
        var res = false
        if (money-price >=0){
            res = true
        }
        return res
    }

    fun lose_healthpoints(){
        //Retire les points de vie lorsque les monstres arrivennt au bout du chemin
        healthpoints-=1
        if(healthpoints<=0){gameover = true}
    }

    fun reset(){
        //Lors d'une nouvelle partie, ces paramètres sont réinitialisés
        money = 100
        healthpoints = 2
        score = 0
        gameover = false
    }
}