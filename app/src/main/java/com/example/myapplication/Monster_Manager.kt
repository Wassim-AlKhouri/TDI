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
    private var monster_money = 10


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
        var explo_monster_dead = 0
        for(monster in view.Monsters){
            if (!monster.dead) {
                new_monsters.add(monster)
            }
            if(monster is Explosif_Monster && monster.dead){explo_monster_dead+=1}
        }
        if (new_monsters.size != view.Monsters.size ){
            view.player.money+=(view.Monsters.size-new_monsters.size-explo_monster_dead)* monster_money
        }
        view.Monsters = new_monsters
    }

    private fun create_monsters() {
        val ran_monster = (1..100).random()
        if (monsters_created <= fibo_series[wave]) {
            if ((SystemClock.elapsedRealtime() - Last_time) >= (300..1250).random()) {
                Last_time = SystemClock.elapsedRealtime()
                monsters_created += 1
                when {
                    wave < 8 -> {
                        when (ran_monster) {
                            in (1..60) -> view.Monsters.add(Normal_Monster(SystemClock.elapsedRealtime(), view, wave))
                            in (61..89) -> if (wave > 3) { view.Monsters.add(Immune_Monster(SystemClock.elapsedRealtime(), view, wave)) } else { view.Monsters.add(Normal_Monster(SystemClock.elapsedRealtime(), view,wave)) }
                            in (62..100) -> if (wave > 5) { view.Monsters.add(Explosif_Monster(SystemClock.elapsedRealtime(), view, wave)) } else { view.Monsters.add(Normal_Monster(SystemClock.elapsedRealtime(), view, wave)) }
                        }
                    }
                    (wave in 8..10) -> {
                        view.player.money += 50
                        when (ran_monster) {
                            in (1..49) -> view.Monsters.add(Normal_Monster(SystemClock.elapsedRealtime(), view, wave))
                            in (50..75) ->  view.Monsters.add(Immune_Monster(SystemClock.elapsedRealtime(), view, wave))
                            in (76..100) ->  view.Monsters.add(Explosif_Monster(SystemClock.elapsedRealtime(), view, wave))
                        }
                    }
                    wave >= 10 -> {
                        view.player.money+=100
                        when (ran_monster) {
                            in (1..33) -> view.Monsters.add(Normal_Monster(SystemClock.elapsedRealtime(), view, wave))
                            in (34..67) -> view.Monsters.add(Immune_Monster(SystemClock.elapsedRealtime(), view, wave))
                            in (68..100) -> view.Monsters.add(Explosif_Monster(SystemClock.elapsedRealtime(), view, wave))
                        }
                    }
                }
            }
        }
        else if (monsters_created > fibo_series[wave] && (SystemClock.elapsedRealtime() - Last_time) > 15000 && view.Monsters.size == 0) {
            monsters_created = 0
            wave += 1
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

    fun reset(){
        wave = 0
        Last_time = SystemClock.elapsedRealtime()
        monsters_created = 0
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