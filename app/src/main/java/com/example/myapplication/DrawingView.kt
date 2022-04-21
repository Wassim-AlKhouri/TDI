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

    init {
        backgroundPaint.color = Color.WHITE
    }

    override fun onSizeChanged(w: Int,h: Int,oldw: Int,oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.Step = (w/Col).toFloat()
        val Li = ((h/this.Step)-1).toInt()
        this.map= Map(Col,Li)
        map.Step = this.Step
        map.creat_Cells()
        this.Monsters.add(Monster(this.Step,this.map.road))
        val canvasH = (h-200).toFloat()
        val canvasW = (w - 25).toFloat()
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
        while (drawing) {
            draw()
            for(Monster in Monsters){
                Monster.move()
                if (Monster.end){drawing = false}
            }
            Thread.sleep(300)
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