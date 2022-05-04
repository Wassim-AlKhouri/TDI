package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Toast
var TotalTime:Long = 0
class MainActivity : AppCompatActivity() {
    lateinit var drawingView: DrawingView
    lateinit var time: Time
    lateinit var monster_manager: Monster_Manager
    lateinit var tower_manager: Tower_Manager
    var playing = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.MainView)
        time = Time(SystemClock.elapsedRealtime())
        monster_manager = Monster_Manager(drawingView)
        tower_manager = Tower_Manager(drawingView)
        val btn_tower = findViewById<Button>(R.id.button)
        val btn_play = findViewById<Button>(R.id.button2)
        val btn_pause = findViewById<Button>(R.id.button3)
        btn_tower.setOnClickListener { OnClick(drawingView,btn_tower) }
        btn_play.setOnClickListener { OnClick2(drawingView) }
        btn_pause.setOnClickListener { OnClick3(playing) }
    }

    override fun onPause() {
        super.onPause()
        drawingView.pause()
        time.pause()
        monster_manager.pause()
        tower_manager.pause()
    }

    override fun onResume() {
        super.onResume()
        drawingView.resume()
        time.resume()
        monster_manager.resume()
        tower_manager.resume()
    }

    fun toast(text: String) {
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_LONG).show()
    }

    fun OnClick(drawingView: DrawingView, button: Button) {
        drawingView.tower_type+=1
        when(drawingView.tower_type){
            3->{button.text="MoneyTower:100"}
            4->{button.text="IceTower:300"}
            5->{drawingView.tower_type=2;button.text="AttackTower:50"}
        }
    }

    fun OnClick2(drawingView: DrawingView) {
        drawingView.button2pushed = !drawingView.button2pushed
    }

    fun OnClick3(playing: Boolean) {
        if (playing) {
            this.playing = false
            this.onPause()
        } else {
            this.playing = true
            this.onResume()
        }
    }

}