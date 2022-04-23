package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Toast
import java.util.*
import android.util.Log
import android.widget.Button
import java.util.logging.Handler
import kotlin.collections.ArrayList

class DrawingView @JvmOverloads
constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr), SurfaceHolder.Callback,Runnable {
    lateinit var canvas: Canvas
    val backgroundPaint = Paint()
    lateinit var thread: Thread
    var drawing: Boolean = true
    val Col = 7
    lateinit var map:Map
    var Step:Float = 0f
    var Monsters = ArrayList<Monster>()
    var Towers = ArrayList<Tower>()
    var buttonpushed = false
    var button2pushed = false
    var totaltime = 0.0
    var monstercreated = true
    init {
        backgroundPaint.color = Color.WHITE
    }

    override fun onSizeChanged(w: Int,h: Int,oldw: Int,oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val canvasH = (h-250).toFloat()
        val canvasW = (w).toFloat()
        this.Step = (canvasW/Col).toFloat()
        val Li = ((canvasH/this.Step)).toInt()
        this.map= Map(Col,Li,this)
        map.creat_Cells()

    }

    fun pause() {
        drawing = false
        thread.join()
    }

    fun resume() {
        drawing = true
        thread = Thread(this)
        thread.start()
    }

    fun draw() {
        if (holder.surface.isValid) {
            canvas = holder.lockCanvas()
            canvas.drawRect(0F, 0F, canvas.getWidth()*1F,
                canvas.getHeight()*1F, backgroundPaint)
            map.draw(canvas)
            for (Monster in Monsters){Monster.draw(canvas)}
            if (this.totaltime > 1000 && monstercreated){
                this.Monsters.add(Monster(this.totaltime,this))
                monstercreated = false
            }
            holder.unlockCanvasAndPost(canvas)
        }
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                if (buttonpushed) {
                    val x = e.rawX.toInt()
                    val y = e.rawY.toInt() - 300
                    val stepx = (x / Step).toInt()
                    val stepy = (y / Step).toInt()
                    if (map.Cells[(Col * stepy) + stepx].type != 1) {
                        map.Cells[(Col * stepy) + stepx].type = 2
                        buttonpushed = false
                    }
                }
            }
        }
        return true
    }

    override fun run() {
        var previousFrameTime = System.currentTimeMillis()
        while (drawing) {
            val currentTime = System.currentTimeMillis()
            var elapsedTimeMS:Double=(currentTime-previousFrameTime).toDouble()
            totaltime+=elapsedTimeMS
            draw()
            Move_Monseters()
        }
    }

    fun Move_Monseters(){
        if(button2pushed) {
            for (Monster in this.Monsters) {
                Monster.move(this.totaltime)
                /*
                if (Monster.end) {
                    drawing = false
                }

                 */
            }
        }
    }

    override fun surfaceChanged(
        holder: SurfaceHolder, format: Int,
        width: Int, height: Int) {
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        thread = Thread(this)
        thread.start()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        thread.join()
    }
}