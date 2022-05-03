package com.example.myapplication

import android.graphics.Canvas
import android.os.SystemClock

class Tower_Manager(val view: DrawingView):Runnable {
    lateinit var thread: Thread
    var playing = true

    override fun run() {
        while(playing){
            manage_towers()
            Thread.sleep(10)
         }
    }

    fun manage_towers() {
        for (tower in view.Towers){
                tower.attack()
                tower.move_projectile()
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
    /*
    fun draw(canvas: Canvas){
        for (tower in view.Towers){
            tower.draw(canvas)
        }
    }*/

}