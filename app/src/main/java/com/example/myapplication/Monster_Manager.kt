package com.example.myapplication

import android.os.SystemClock

class Monster_Manager(val view:DrawingView):Runnable {
    lateinit var thread: Thread
    var playing = true
    var wave = 0
    val wave_length = 3 // durée de la vague en seconde
    var monsters_per_wave = 1 // nombre de monstres en plus à chaque vague

    override fun run() {
        while (playing){
            create_monsters(TotalTime)
            manage_monsters()
            delete_monsters()
            Thread.sleep(10)
        }
    }

    fun delete_monsters(){
        var new_monsters = ArrayList<Monster>()
        for(monster in view.Monsters){
            if (!monster.dead){ new_monsters.add(monster) }
        }
        if (new_monsters.size != view.Monsters.size){
            view.money+=(view.Monsters.size-new_monsters.size)*50
        }
        view.Monsters = new_monsters
    }

    fun create_monsters(time:Long){
        if (time > (wave_length*wave) && view.Monsters.size == 0){
            this.wave+=1
            for(i in 0..wave*monsters_per_wave){
                view.Monsters.add(Explosif_Monster(SystemClock.elapsedRealtime(),view))
                Thread.sleep(1000)
            }
        }
    }

    fun manage_monsters(){
        for (monster in view.Monsters) {
            monster.move()
            for (tower in view.Towers) {
                if(tower !is Money_Tower){tower.detect_monster(monster)}
            }
        }
    }

    fun resume(){
        playing = true
        thread = Thread(this)
        thread.start()
    }

    fun pause() {
        playing = false
        thread.join()
    }

}