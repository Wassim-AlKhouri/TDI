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
        val Button2 = findViewById<Button>(R.id.button2)
        val Button = findViewById<Button>(R.id.button)
        Button2.setOnClickListener {}
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
fun OnClick2(step:Float,road: java.util.ArrayList<Array<Int>>){

}

fun OnClick(){
    buttonpushed = !buttonpushed
}