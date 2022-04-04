package com.example.myapplication

abstract class Tower (Place: List<Int>, Type:Int, radius:Double) {
    var target:Monster? = null

    fun Attack(){
        target?.lose_HP()
    }

    fun chnage_target(){}

}

class BasicTower(Place: List<Int>, Type:Int, radius:Double):Tower(Place, Type, radius){

}

class FireTower(Place: List<Int>, Type:Int, radius:Double):Tower(Place, Type, radius){

}

class IceTower(Place: List<Int>, Type:Int, radius:Double):Tower(Place, Type, radius){

}
