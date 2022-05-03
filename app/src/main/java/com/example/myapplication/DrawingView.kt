package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.SystemClock
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
    val blackPaint = Paint()
    lateinit var thread: Thread
    var drawing: Boolean = true
    val Col = 7
    lateinit var map:Map
    var Step:Float = 0f
    var Monsters = ArrayList<Monster>()
    var Towers = ArrayList<Tower>()
    var tower_type = 2
    var buttonpushed = false
    var button2pushed = false
    var money = 500

    init {
        backgroundPaint.color = Color.WHITE
        blackPaint.color = Color.BLACK
        blackPaint.textSize = 50f
        //this.Step = (canvas.width/Col).toFloat()
    }

    override fun run() {
        while (drawing) { draw() }
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

    fun draw() {
        if (holder.surface.isValid) {
            canvas = holder.lockCanvas()
            canvas.drawRect(0F, 0F, canvas.getWidth()*1F, canvas.getHeight()*1F,backgroundPaint)
            map.draw(canvas)
            for (monster in Monsters) {
                Thread.sleep(10)
                monster.draw(canvas)
            }
            for (tower in Towers){tower.draw(canvas)
            Thread.sleep(10)}

            //monstercreated = false
            draw_time(canvas)
            draw_money(canvas)
            holder.unlockCanvasAndPost(canvas)
        }
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = e.rawX.toInt()
                val y = e.rawY.toInt() - 300
                val stepx = (x / Step).toInt()
                val stepy = (y / Step).toInt()
                var price=0
                val position:List<Int> = listOf(stepx,stepy)
                when(tower_type){
                    2->price=50
                    3->price=100
                    4->price=300
                }
                if ((money-price) >= 0) {
                    if (map.Cells[(Col * stepy) + stepx].type != 1) {
                        map.Cells[(Col * stepy) + stepx].type = tower_type
                        when(tower_type){
                            2->Towers.add(Attack_Tower(position,this))
                            3->Towers.add(Money_Tower(position,this,SystemClock.elapsedRealtime()))
                            4->Towers.add(Ice_Tower(position,this))
                        }
                        money-=price
                    }
                }
            }
        }
        return true
    }

    fun draw_time(canvas: Canvas){
        canvas.drawText(TotalTime.toString(),5f,50f,blackPaint)
    }

    fun draw_money(canvas: Canvas){
        canvas.drawText(money.toString(),(canvas.width/2).toFloat(),(canvas.height - 100).toFloat(),blackPaint)
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