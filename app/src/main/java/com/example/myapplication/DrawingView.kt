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
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicReferenceArray
import java.util.logging.Handler
import kotlin.collections.ArrayList

class DrawingView @JvmOverloads
constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr), SurfaceHolder.Callback,Runnable {

    lateinit var canvas: Canvas
    private val backgroundPaint = Paint()
    private val blackPaint = Paint()
    lateinit var thread: Thread
    var drawing: Boolean = true
    val Col = 7
    lateinit var map:Map
    var Step:Float = 0f
    var Monsters = CopyOnWriteArrayList<Monster>()
    var Towers = CopyOnWriteArrayList<Tower>()
    var tower_type = 2
    val player = Player()

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
        val canvasH = (h-100).toFloat()
        val canvasW = (w).toFloat()
        this.Step = (canvasW/Col).toFloat()
        val Li = ((canvasH/this.Step)).toInt()
        this.map= Map(Col,Li,this)
        //this.map.creat_Cells()
    }

    fun draw() {
        if (holder.surface.isValid) {
            canvas = holder.lockCanvas()
            canvas.drawRect(0F, 0F, canvas.getWidth()*1F, canvas.getHeight()*1F,backgroundPaint)
            map.draw(canvas)
            for (monster in Monsters) { monster.draw(canvas) }
            for (tower in Towers){if(tower is Attack_Tower){tower.draw(canvas)}}
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
                    3->price = 300
                    4->price= 250
                }
                if (player.afford(price)) {
                    if (((Col * stepy) + stepx) < map.Cells.size && map.Cells[(Col * stepy) + stepx].type == 0) {
                        //map.Cells[(Col * stepy) + stepx].type = tower_type
                        when(tower_type){
                            2->Towers.add(Normal_Attack_Tower(position,this))
                            3->Towers.add(Money_Tower(position,this,SystemClock.elapsedRealtime()))
                            4->Towers.add(Ice_Tower(position,this))
                        }
                    }
                }
            }
        }
        return true
    }

    fun draw_time(canvas: Canvas) {
        val a = 0
        if (TotalTime[1] < 10 && TotalTime[0] < 10){
            canvas.drawText(("0${TotalTime[0]}: 0${TotalTime[1]}"), 5f, 50f, blackPaint)
        }
        else if (TotalTime[1] < 10 && TotalTime[0] > 10){
            canvas.drawText(("${TotalTime[0]}: 0${TotalTime[1]}"), 5f, 50f, blackPaint)
        }
        else if (TotalTime[1] > 10 && TotalTime[0] < 10){
            canvas.drawText(("0${TotalTime[0]}: ${TotalTime[1]}"), 5f, 50f, blackPaint)
        }
        else {
            canvas.drawText(("${TotalTime[0]}:${TotalTime[1]}"),5f,50f,blackPaint)
        }
    }

    fun draw_money(canvas: Canvas){
        canvas.drawText(player.money.toString(),(canvas.width/2).toFloat(),(canvas.height - 100).toFloat(),blackPaint)
    }

    fun reset(){
        map.reset()
        player.reset()
        Monsters = CopyOnWriteArrayList<Monster>()
        Towers = CopyOnWriteArrayList<Tower>()
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