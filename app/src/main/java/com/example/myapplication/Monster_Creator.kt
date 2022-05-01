package com.example.myapplication

import android.os.SystemClock

class Monster_Creator(val view:DrawingView):Runnable {
    lateinit var thread: Thread
    var playing = true
    var wave = 0
    val wave_length = 5 // durée de la vague en seconde
    var monsters_per_wave = 2 // nombre de monstres en plus à chaque vague
    var monster_manager = Monster_Manager(this,view)

    override fun run() { while (playing){ create_monsters(TotalTime);delete_monsters() } }

    fun delete_monsters(){
        for(monster in view.Monsters){
            if (monster.dead){
                view.Monsters.remove(monster)
            }
        }
    }

    fun create_monsters(time:Long){
        if (time > (wave_length*(1+wave)) && view.Monsters.size == 0){
            wave+=1
            while(view.Monsters.size <= (1+wave)*monsters_per_wave){
                view.Monsters.add(Monster(SystemClock.currentThreadTimeMillis(),view))
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