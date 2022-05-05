package com.example.myapplication

import android.graphics.Canvas
import android.os.SystemClock
import java.util.concurrent.CopyOnWriteArrayList

class Monster_Manager(val view:DrawingView, monster: Monster):Runnable {
    lateinit var thread: Thread
    private var playing = true
    private var wave = monster.wave
    private val fibo_series = ArrayList<Int>()
    private var Last_time :Long = 0
    private var monsters_created = 0


    init {
        this.fibonacci()
        Last_time = SystemClock.elapsedRealtime()
    }

    override fun run() {
        while (playing){
            create_monsters()
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
            view.money+=(view.Monsters.size-new_monsters.size)*5
        }
        view.Monsters = new_monsters
    }

    private fun create_monsters(){
        val ran_monster = (1..3).random()
        if(monsters_created <= fibo_series[wave]) {
            if (ran_monster == 1) {
                if ((SystemClock.elapsedRealtime() - Last_time) >= (750..1250).random()) {
                    view.Monsters.add(Normal_Monster(SystemClock.elapsedRealtime(), view))
                    Last_time = SystemClock.elapsedRealtime()
                    monsters_created += 1
                }
            } else if (ran_monster === 2) {
                if ((SystemClock.elapsedRealtime() - Last_time) >= (750..1250).random()) {
                    view.Monsters.add(Immune_Monster(SystemClock.elapsedRealtime(), view))
                    Last_time = SystemClock.elapsedRealtime()
                    monsters_created += 1
                }
            }
            else if (ran_monster == 3) {
                if ((SystemClock.elapsedRealtime() - Last_time) >= (750..1250).random()) {
                    view.Monsters.add(Explosif_Monster(SystemClock.elapsedRealtime(), view))
                    Last_time = SystemClock.elapsedRealtime()
                    monsters_created += 1
                }
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