package com.example.myapplication

import android.os.SystemClock
import android.widget.TextView

class Time( initial_time:Long):Runnable {
    lateinit var thread: Thread
    private var playing = true
    private var last_time:Long = initial_time

    override fun run() { while(playing){update_time()}}

    private fun update_time(){
        // Met à jour le temps
        TotalTime[1] += (SystemClock.elapsedRealtime()  - last_time).toInt()/1000
        if(TotalTime[1]>=60){ TotalTime[0]+=1;TotalTime[1]-=60}
        last_time = SystemClock.elapsedRealtime()
        Thread.sleep(1000)
    }

    fun resume(){
        playing = true
        thread = Thread(this)
        thread.start()
        this.last_time = SystemClock.elapsedRealtime()
    }

    fun pause() {
        playing = false
        thread.join()
    }

    fun reset(){
        //Remet tous les paramètres à zéro
        TotalTime = arrayOf(0,0)
        last_time = SystemClock.elapsedRealtime()
    }

}