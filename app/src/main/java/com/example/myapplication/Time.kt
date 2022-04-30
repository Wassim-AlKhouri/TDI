package com.example.myapplication

import android.os.SystemClock

class Time(val initial_time:Long):Runnable {
    lateinit var thread: Thread
    var playing = true
    fun update_time(){ TotalTime = (SystemClock.elapsedRealtime()  - initial_time)/1000 }
    override fun run() { while(playing){update_time()} }
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