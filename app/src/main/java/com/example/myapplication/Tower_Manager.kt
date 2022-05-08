package com.example.myapplication

import android.graphics.Canvas
import android.os.SystemClock

class Tower_Manager(val view: DrawingView):Runnable {
    //Tower_Manager fonctionne sur un thread à part et s'occupe de faire attaquer les tours et de faire bouger leurs projectiles
    lateinit var thread: Thread
    private var playing = true
    override fun run() {
        while (playing) {
            manage_towers()
        }
    }


    private fun manage_towers() {
        for (tower in view.Towers){
                tower.attack()
                if(tower is Attack_Tower){tower.move_projectile()}
        }
        for (tower in view.Sacrifice_Towers){
            tower.attack()
        }
    }

    fun resume(){
        playing = true
        thread = Thread(this)
        thread.start()
        for (tower in view.Towers){
            // remet le temps du dernier mouvement à maintenant
            if(tower is Attack_Tower && tower.projectile!=null){
                tower.projectile!!.last_time = SystemClock.elapsedRealtime()
            }
            else if (tower is Money_Tower){
                tower.last_time=SystemClock.elapsedRealtime()
            }
        }
    }

    fun pause() {
        playing = false
        thread.join()
    }

}