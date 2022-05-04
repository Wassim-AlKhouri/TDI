package com.example.myapplication

import android.os.SystemClock
import android.widget.TextView

class Time(val initial_time:Long):Runnable {
    lateinit var thread: Thread
    var playing = true
    var last_time:Long = initial_time

    override fun run() { while(playing){update_time()} }

    fun update_time(){
        TotalTime += (SystemClock.elapsedRealtime()  - last_time)/1000
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

}