package com.example.myapplication

import android.os.SystemClock

abstract class Utility_Tower(override val Position: List<Int>, override val view: DrawingView):Tower(Position, view){
    abstract val specialattack_interval: Long
    var last_time = SystemClock.elapsedRealtime()
    override fun attack(){
        if ((SystemClock.elapsedRealtime()-last_time) >= specialattack_interval){
            last_time = SystemClock.elapsedRealtime()
            special_ability()
        }
    }
    abstract fun special_ability()
}

class Money_Tower(override val Position: List<Int>, override val view: DrawingView):Utility_Tower(Position,view){
    override val Step = view.Step
    var money_gen: Int = 5
    override val specialattack_interval: Long = 2000
    override val type: Int = 3
    override val name ="$$"
    init {
        view.map.Cells[(view.map.Col * Position[1]) + Position[0]].type = type
    }

    override fun special_ability() {
        view.player.money+=money_gen
    }

    override fun upgrade() {
        money_gen+=5
        level+=1
    }

    override fun explode() {
        view.Towers.remove(this)
        view.map.Cells[(view.Col * Position[1]) + Position[0]].type = 0
    }

}

class Sacrifice_Tower(override val Position: List<Int>, override val view: DrawingView):Utility_Tower(Position,view){
    override val Step = view.Step
    override val specialattack_interval: Long = 50000
    override val type: Int = 5
    override val name ="ST"
    var lives = 1
    init {
        view.map.Cells[(view.map.Col * Position[1]) + Position[0]].type = type
    }

    override fun special_ability() {
        view.Towers.random().upgrade()
    }

    override fun upgrade() {
        lives+=1
        level+=1
    }

    override fun explode(){
        lives-=1
        if(lives==0){
            view.Sacrifice_Towers.remove(this)
            view.map.Cells[(view.Col * Position[1]) + Position[0]].type = 0
        }
    }
}