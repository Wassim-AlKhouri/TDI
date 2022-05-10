package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.CopyOnWriteArrayList

class DrawingView @JvmOverloads
constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr), SurfaceHolder.Callback,Runnable,Price {

    lateinit var canvas: Canvas
    private val backgroundPaint = Paint()
    private val whitePaint = Paint()
    lateinit var thread: Thread
    private var drawing: Boolean = true
    val Col = 7  // nombre de colonnes dans le map
    lateinit var map: Map //
    var Step: Float = 0f  // le côté de la case
    var Monsters = CopyOnWriteArrayList<Monster>()  // liste de monstres vivants (CopyOnWriteList a été utilisé car il est thread-safe)
    var Towers = CopyOnWriteArrayList<Tower>()  // liste des tours
    var Sacrifice_Towers = CopyOnWriteArrayList<Tower>()  // liste des tours de sacrifice
    var tower_type = 2 // ce variable sert à savoir quel type de tour va être contruit quand on appuie sur l'écran
    val player = Player()
    private val activity = context as FragmentActivity
    var upgrade = false // si le bouton upgrade est active

    init {
        backgroundPaint.color = Color.BLACK
        whitePaint.color = Color.WHITE
        whitePaint.textSize = 50f

    }

    override fun run() {
        while (drawing) {
            draw()
            if (player.gameover) {
                player.gameover = false
                activity.runOnUiThread(Runnable {
                    /*
                    runOnUiThread nous permet de lance ce bout de code sur le thread principale et
                    donc de faire appel à des méthode du MainActivity
                    */
                    (activity as MainActivity).gameover()
                })
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // onSizeChanged nous donne les dimensions de l'écran avec les quels on crée Step et map
        this.Step = (w / Col).toFloat()
        val Li = ((h/this.Step)).toInt() - 2 // nombre de lignes dans le map qui dépend de la taille de l'ècran
        this.map = Map(Col, Li, this)
    }

    private fun draw() {
        // dessine tout les monstres/tours/map et affichent divers information(argent,temps,...)
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
                val y = e.rawY.toInt()-300 // le "-300" a été ajouté à cause du décalage entre les données récuppérées et la position réel d'appuie
                val stepx = (x / Step).toInt()
                val stepy = (y / Step).toInt()
                val position: List<Int> = listOf(stepx, stepy)
                if (!upgrade && player.afford(get_price(tower_type))) {
                    // teste si upgrade est false et si le joueur a assez d'argent pour créer une tour
                        // get_price est une méthode que drawingView hérite de l'interface Price
                    if (((Col * stepy) + stepx) < map.Cells.size && map.Cells[(Col * stepy) + stepx].type == 0) {
                        //teste si le joueur a appuyé à l'interieur du map et si la case est de type sol
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
                        // le "for" cherche la tour dont la position correspond à celle de l'appuie
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
        // dessine le temps en haut à gauche
        if (TotalTime[1] < 10 && TotalTime[0] < 10) {
            canvas.drawText(("0${TotalTime[0]}: 0${TotalTime[1]}"), 5f, 50f, whitePaint)
        } else if (TotalTime[1] < 10 && TotalTime[0] > 10) {
            canvas.drawText(("${TotalTime[0]}: 0${TotalTime[1]}"), 5f, 50f, whitePaint)
        } else if (TotalTime[1] > 10 && TotalTime[0] < 10) {
            canvas.drawText(("0${TotalTime[0]}: ${TotalTime[1]}"), 5f, 50f, whitePaint)
        } else {
            canvas.drawText(("${TotalTime[0]}:${TotalTime[1]}"), 5f, 50f, whitePaint)
        }
    }

    private fun draw_money(canvas: Canvas) {
        // dessine combien d'agent le joueur poosède
        canvas.drawText("Money:${player.money}", 10f, (canvas.height - 200).toFloat(), whitePaint)
    }

    private fun draw_healthpoints(canvas: Canvas) {
        // dessine les points de vie restantes du joueur
        canvas.drawText(
            "Healthpoints:${player.healthpoints}",
            (canvas.width - 350).toFloat(),
            (canvas.height - 200).toFloat(),
            whitePaint
        )
    }

    private fun draw_score(canvas: Canvas) {
        // dessine le score
        canvas.drawText(
            "Score:${player.score}",
            ((canvas.width / 2) - 150).toFloat(),
            (canvas.height - 200).toFloat(),
            whitePaint
        )
    }

    fun reset() {
        // réinitialise les attribues de DrawingView pour lancer une nouvelle partie
        map.reset()
        player.reset()
        Monsters = CopyOnWriteArrayList<Monster>()
        Towers = CopyOnWriteArrayList<Tower>()
        Sacrifice_Towers = CopyOnWriteArrayList<Tower>()
        tower_type = 2
        upgrade = false
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

