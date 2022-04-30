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
    lateinit var thread: Thread
    var drawing: Boolean = true
    val Col = 7
    lateinit var map:Map
    var Step:Float = 0f
    var Monsters = ArrayList<Monster>()
    var Towers = ArrayList<Tower>()
    var buttonpushed = false
    var button2pushed = false
    var monstercreated = false

    init {
        backgroundPaint.color = Color.WHITE
        backgroundPaint.textSize = 50f
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
            if ( !monstercreated && Monsters.size < 1){
                this.Monsters.add(Monster(SystemClock.elapsedRealtime(),this))
                monstercreated = true
            }
            for (monster in Monsters){monster.draw(canvas)}
            for (tower in Towers){tower.draw(canvas)}
            monstercreated = false
            draw_time(canvas)
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
                        val position:List<Int> = listOf(stepx,stepy)
                        Towers.add(Tower(position,this ))
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
            Move_Monsters()
            Move_Projectile()
        }
    }

    fun Move_Monsters(){
            for (monster in this.Monsters) {
                    monster.move()
                    for (tower in Towers) {
                        tower.detect_monster(monster)
                    }
                    if (monster.dead) { this.Monsters.remove(monster) }
            }
    }

    fun Move_Projectile() {
        for (tower in Towers){
            tower.attack()
            tower.move_projectile()
        }
    }
    fun draw_time(canvas: Canvas){
        canvas.drawText(TotalTime.toString(),5f,50f,backgroundPaint)
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