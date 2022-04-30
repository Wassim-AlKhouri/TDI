package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.Toast
var TotalTime:Long = 0
class MainActivity : AppCompatActivity() {
    lateinit var drawingView: DrawingView
    lateinit var time: Time
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.MainView)
        time = Time(SystemClock.elapsedRealtime())
        val btn_tower = findViewById<Button>(R.id.button)
        val btn_play = findViewById<Button>(R.id.button2)
        val btn_pause = findViewById<Button>(R.id.button3)
        btn_tower.setOnClickListener{OnClick(drawingView)}
        btn_play.setOnClickListener { OnClick2(drawingView)}
        btn_pause.setOnClickListener {}
    }

    override fun onPause() {
        super.onPause()
        drawingView.pause()
        time.pause()
    }

    override fun onResume() {
        super.onResume()
        drawingView.resume()
        time.resume()
    }
    fun toast(text:String){
        Toast.makeText(this@MainActivity,text, Toast.LENGTH_LONG).show()
    }
}
fun OnClick2(drawingView: DrawingView){
    drawingView.button2pushed = !drawingView.button2pushed
}

fun OnClick(drawingView: DrawingView){
    drawingView.buttonpushed = !drawingView.buttonpushed
}