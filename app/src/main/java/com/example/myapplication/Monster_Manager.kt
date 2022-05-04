package com.example.myapplication

import android.graphics.Canvas
import android.os.SystemClock
import java.util.concurrent.CopyOnWriteArrayList

class Monster_Manager(val view:DrawingView):Runnable {
    lateinit var thread: Thread
    var playing = true
    var wave = 0
    val fibo_series = ArrayList<Int>()
    var Last_time :Long = 0
    var monsters_created = 0

    init {
        this.fibonacci(100)
        Last_time = SystemClock.elapsedRealtime()
    }

    override fun run() {
        while (playing){
            create_monsters(TotalTime)
            manage_monsters()
            delete_monsters()
        }
    }

    fun delete_monsters(){
        val new_monsters = CopyOnWriteArrayList<Monster>()
        for(monster in view.Monsters){
            if (!monster.dead){ new_monsters.add(monster) }
        }
        if (new_monsters.size != view.Monsters.size){
            view.money+=(view.Monsters.size-new_monsters.size)*50
        }
        view.Monsters = new_monsters
    }

    fun create_monsters(time:Long){
        if(monsters_created <= fibo_series[wave]) {
            if ( (SystemClock.elapsedRealtime() - Last_time) >= (750..1250).random()) {
                view.Monsters.add(Normal_Monster(SystemClock.elapsedRealtime(), view))
                Last_time = SystemClock.elapsedRealtime()
                monsters_created+=1
            }
        }
        else if (monsters_created > fibo_series[wave] && (SystemClock.elapsedRealtime() - Last_time) > 15000){
            monsters_created = 0
            wave+=1
            Last_time = SystemClock.elapsedRealtime()
        }
    }

    fun manage_monsters(){
        for (monster in view.Monsters) {
            monster.move()
            for (tower in view.Towers) {
                if(tower is Attack_Tower){tower.detect_monster(monster)}
            }
        }
    }
    /*
    fun draw(canvas: Canvas){
        for (monster in view.Monsters){
            monster.draw(canvas)
        }
    }*/

    fun resume(){
        playing = true
        thread = Thread(this)
        thread.start()
    }

    fun pause() {
        playing = false
        thread.join()
    }

    fun fibonacci(n: Int){
        var t1 = 1
        var t2 = 2
        fibo_series.add(t1)
        for (i in 0..n){
            var sum = t1+t2
            t1 = t2
            t2 = sum
            fibo_series.add(t1)
        }
    }

}