package com.example.myapplication

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
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
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicReferenceArray
import java.util.logging.Handler
import kotlin.collections.ArrayList

class DrawingView @JvmOverloads
constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr), SurfaceHolder.Callback,Runnable,Price {

    lateinit var canvas: Canvas
    private val backgroundPaint = Paint()
    private val blackPaint = Paint()
    lateinit var thread: Thread
    var drawing: Boolean = true
    val Col = 7
    val Li = 10
    lateinit var map: Map
    var Step: Float = 0f
    var Monsters = CopyOnWriteArrayList<Monster>()
    var Towers = CopyOnWriteArrayList<Tower>()
    var Sacrifice_Towers = CopyOnWriteArrayList<Tower>()
    var tower_type = 2
    val player = Player()
    val activity = context as FragmentActivity
    var upgrade = false


    init {
        backgroundPaint.color = Color.WHITE
        blackPaint.color = Color.BLACK
        blackPaint.textSize = 50f
    }

    override fun run() {
        while (drawing) {
            draw()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val canvasH = (h - 100).toFloat()
        val canvasW = (w).toFloat()
        this.Step = (canvasW / Col).toFloat()
        //val Li = ((canvasH/this.Step)).toInt()
        this.map = Map(Col, Li, this)
        //this.map.creat_Cells()
    }

    private fun draw() {
        if (holder.surface.isValid) {
            canvas = holder.lockCanvas()
            canvas.drawRect(
                0F,
                0F,
                canvas.getWidth() * 1F,
                canvas.getHeight() * 1F,
                backgroundPaint
            )
            map.draw(canvas)
            for (monster in Monsters) {
                monster.draw(canvas)
            }
            for (tower in Towers) {
                tower.draw(canvas)
                if (tower is Attack_Tower) {
                    tower.draw_projectile(canvas)
                }
            }
            for (tower in Sacrifice_Towers) {
                tower.draw(canvas)
            }
            draw_time(canvas)
            draw_money(canvas)
            draw_healthpoints(canvas)
            draw_score(canvas)
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
                //var price=0
                val position: List<Int> = listOf(stepx, stepy)
                if (!upgrade && player.afford(get_price(tower_type))) {
                    if (((Col * stepy) + stepx) < map.Cells.size && map.Cells[(Col * stepy) + stepx].type == 0) {
                        //map.Cells[(Col * stepy) + stepx].type = tower_types
                        player.money -= get_price(tower_type)
                        when (tower_type) {
                            2 -> Towers.add(Normal_Attack_Tower(position, this))
                            3 -> Towers.add(Money_Tower(position, this))
                            4 -> Towers.add(Ice_Tower(position, this))
                            5 -> Sacrifice_Towers.add(Sacrifice_Tower(position, this))
                        }
                    }
                } else if (upgrade) {
                    for (tower in Towers.plus(Sacrifice_Towers)) {
                        if (position == tower.Position) {
                            tower.ask_for_upgrade()
                        }
                    }
                }
            }
        }
        return true
    }

    private fun draw_time(canvas: Canvas) {
        val a = 0
        if (TotalTime[1] < 10 && TotalTime[0] < 10) {
            canvas.drawText(("0${TotalTime[0]}: 0${TotalTime[1]}"), 5f, 50f, blackPaint)
        } else if (TotalTime[1] < 10 && TotalTime[0] > 10) {
            canvas.drawText(("${TotalTime[0]}: 0${TotalTime[1]}"), 5f, 50f, blackPaint)
        } else if (TotalTime[1] > 10 && TotalTime[0] < 10) {
            canvas.drawText(("0${TotalTime[0]}: ${TotalTime[1]}"), 5f, 50f, blackPaint)
        } else {
            canvas.drawText(("${TotalTime[0]}:${TotalTime[1]}"), 5f, 50f, blackPaint)
        }
    }

    private fun draw_money(canvas: Canvas) {
        canvas.drawText("Money:${player.money}", 10f, (canvas.height - 200).toFloat(), blackPaint)
    }

    private fun draw_healthpoints(canvas: Canvas) {
        canvas.drawText(
            "Healthpoints:${player.healthpoints}",
            (canvas.width - 350).toFloat(),
            (canvas.height - 200).toFloat(),
            blackPaint
        )
    }

    private fun draw_score(canvas: Canvas) {
        canvas.drawText(
            "Score:${player.score}",
            ((canvas.width / 2) - 90).toFloat(),
            (canvas.height - 200).toFloat(),
            blackPaint
        )
    }

    fun reset() {
        map.reset()
        player.reset()
        Monsters = CopyOnWriteArrayList<Monster>()
        Towers = CopyOnWriteArrayList<Tower>()
        Sacrifice_Towers = CopyOnWriteArrayList<Tower>()
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
        width: Int, height: Int
    ) {
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        thread = Thread(this)
        thread.start()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        thread.join()
    }
}
    /*fun showGameOverDialog(messageId: Int) {
        class GameResult: DialogFragment() {
            override fun onCreateDialog(bundle: Bundle?): Dialog {
                val builder = AlertDialog.Builder(getActivity())
                builder.setTitle(resources.getString(messageId))
                builder.setMessage("Score : ${player.score}")
                builder.setPositiveButton("Redémarrer le jeu",
                    DialogInterface.OnClickListener { _, _-> mainActivity.new_game()}
                )
                return builder.create()
            }
        }

        activity.runOnUiThread(
            Runnable {
                val ft = activity.supportFragmentManager.beginTransaction()
                val prev =
                    activity.supportFragmentManager.findFragmentByTag("dialog")
                if (prev != null) {
                    ft.remove(prev)
                }
                ft.addToBackStack(null)
                val gameResult = GameResult()
                gameResult.setCancelable(false)
                gameResult.show(ft,"dialog")
            }
        )
    }
    */