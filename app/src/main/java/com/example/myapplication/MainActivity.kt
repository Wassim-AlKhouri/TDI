package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast

var buttonpushed = false
class MainActivity : AppCompatActivity() {
    lateinit var drawingView: DrawingView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.MainView)
        val Button = findViewById<Button>(R.id.button)
        Button.setOnClickListener{OnClick()}
    }

    override fun onPause() {
        super.onPause()
        drawingView.pause()
    }

    override fun onResume() {
        super.onResume()
        drawingView.resume()
    }
    fun toast(text:String){
        Toast.makeText(this@MainActivity,text, Toast.LENGTH_LONG).show()
    }
}
fun OnClick(){
    buttonpushed = !buttonpushed
}
