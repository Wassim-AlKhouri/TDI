package com.example.myapplication

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentManager

var TotalTime = arrayOf(0,0)
class MainActivity : AppCompatActivity() {
    lateinit var drawingView: DrawingView
    lateinit var time: Time
    private lateinit var monster_manager: Monster_Manager
    private lateinit var tower_manager: Tower_Manager
    private var playing = true

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
        val pause_fragment = Pause_fragment()
        val manager = supportFragmentManager
        btn_tower.setOnClickListener { OnClick(drawingView,btn_tower) }
        btn_play.setOnClickListener {  }
        btn_pause.setOnClickListener { OnClick3(pause_fragment)}
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
            3->{button.text="MoneyTower:300"}
            4->{button.text="IceTower:250"}
            5->{drawingView.tower_type=2;button.text="AttackTower:50"}
        }
    }

    fun OnClick2(pause_fragment: Pause_fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment,pause_fragment)
            commit()
        }

    }

    fun new_game(){
        onPause()
        drawingView.reset()
        time.reset()
        monster_manager.reset()
        Thread.sleep(1000)
        onResume()
        playing = true
    }

    fun resume(){
        onResume()
        playing = true
    }

    fun OnClick3(pause_fragment: Pause_fragment) {
        if (playing) {
            this.playing = false
            this.onPause()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment, pause_fragment)
                commit()
            }
        }
    }
}