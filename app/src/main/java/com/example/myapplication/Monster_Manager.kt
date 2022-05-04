package com.example.myapplication

import android.graphics.Canvas
import android.os.SystemClock
import java.util.concurrent.CopyOnWriteArrayList

class Monster_Manager(val view:DrawingView):Runnable {
    lateinit var thread: Thread
    private var playing = true
    private var wave = 0
    private val fibo_series = ArrayList<Int>()
    private var Last_time :Long = 0
    private var monsters_created = 0

    init {
        this.fibonacci()
        Last_time = SystemClock.elapsedRealtime()
    }

    override fun run() {
        while (playing){
            create_monsters(TotalTime)
            manage_monsters()
            delete_monsters()
        }
    }

    private fun delete_monsters(){
        val new_monsters = CopyOnWriteArrayList<Monster>()
        for(monster in view.Monsters){
            if (!monster.dead){ new_monsters.add(monster) }
        }
        if (new_monsters.size != view.Monsters.size){
            view.money+=(view.Monsters.size-new_monsters.size)*50
        }
        view.Monsters = new_monsters
    }

    private fun create_monsters(time:Long){
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

    private fun manage_monsters(){
        for (monster in view.Monsters) {
            monster.move()
            for (tower in view.Towers) {
                if(tower is Attack_Tower){tower.detect_monster(monster)}
            }
        }
    }

    fun resume(){
        playing = true
        thread = Thread(this)
        thread.start()
        for (monster in view.Monsters){
            monster.LastMouvement = SystemClock.elapsedRealtime()
        }
    }

    fun pause() {
        playing = false
        thread.join()
    }

    private fun fibonacci(){
        var t1 = 1
        var t2 = 2
        fibo_series.add(t1)
        for (i in 0..100){
            var sum = t1+t2
            t1 = t2
            t2 = sum
            fibo_series.add(t1)
        }
    }

}