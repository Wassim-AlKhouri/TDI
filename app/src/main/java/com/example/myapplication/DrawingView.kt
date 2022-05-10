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
    var wave = 0 // Numéro de la vague d'ennemis
    val Col = 7  // nombre de colonnes dans le map
    lateinit var map: Map
    var Step: Float = 0f  // la longueur du côté de la case
    var Monsters = CopyOnWriteArrayList<Monster>()  // liste de monstres vivants (CopyOnWriteList a été utilisé car il est thread-safe)
    var Towers = CopyOnWriteArrayList<Tower>()  // liste des tours
    var Upgrade_Towers = CopyOnWriteArrayList<Tower>()  // liste des tours d'amélioration
    var tower_type = 2 // ce variable sert à savoir quel type de tour va être contruit quand on appuie sur l'écran
    val player = Player()
    private val activity = context as FragmentActivity
    var upgrade = false // si le bouton upgrade est active
    private var immune_fragment = true
    private var explosif_fragment = true
    var High_Score : Int = 0 // le high score sera redéfinit dans le MainActivity

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
                if(player.score > High_Score){
                    High_Score = player.score
                }
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
        // onSizeChanged nous donne les dimensions de l'écran avec lesquels on crée Step et map
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
                if (monster is Immune_Monster && immune_fragment){
                    immune_fragment = false
                    val Title = "Wave 4"
                    val Message = """ 
                     Be wary! Immune monsters have appeared! 
                     They are immmune to any kind of damage when they turn grey. 
                     They are very strong, do not underestimate them!
                    """.trimIndent()
                    activity.runOnUiThread(Runnable { (activity as MainActivity).Show_info(Title, Message)})
                }
                if (monster is Explosif_Monster && explosif_fragment){
                    explosif_fragment = false
                    val Title = "Wave 6"
                    val Message = """ 
                     Caution! Explosif monsters have appeared! 
                     If you let them live long enough, they will explode and take out one of your towers with them. 
                     Tip : They will prioritize upgrade towers.
                    """.trimIndent()
                    activity.runOnUiThread(Runnable { (activity as MainActivity).Show_info(Title, Message)})
                }
            }
            for (tower in Towers) {
                tower.draw(canvas)
                if (tower is Attack_Tower) {
                    tower.draw_projectile(canvas)
                }
            }
            for (tower in Upgrade_Towers) {
                tower.draw(canvas)
            }
            draw_time(canvas)
            draw_money(canvas)
            draw_healthpoints(canvas)
            draw_score(canvas)
            draw_wave(canvas)
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
                        // get_price est une méthode que drawingView implémente de l'interface Price
                    if (((Col * stepy) + stepx) < map.Cells.size && map.Cells[(Col * stepy) + stepx].type == 0) {
                        //teste si le joueur a appuyé à l'interieur de la map et si la case est de type sol
                        player.money -= get_price(tower_type) // le joueur paye le prix de construction de la tour
                        when (tower_type) {
                            2 -> Towers.add(Normal_Attack_Tower(position, this))
                            3 -> Towers.add(Money_Tower(position, this))
                            4 -> Towers.add(Ice_Tower(position, this))
                            5 -> Upgrade_Towers.add(Upgrade_Tower(position, this))
                        }
                    }
                } else if (upgrade) {
                    for (tower in Towers.plus(Upgrade_Towers)) {
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
        canvas.drawText("MONEY : ${player.money}", 10f, (canvas.height - 200).toFloat(), whitePaint)
    }

    private fun draw_healthpoints(canvas: Canvas) {
        // dessine les points de vie restants du joueur
        canvas.drawText(
            "HP : ${player.healthpoints}",
            (canvas.width - 150).toFloat(),
            (canvas.height - 200).toFloat(),
            whitePaint
        )
    }

    private fun draw_score(canvas: Canvas) {
        // dessine le score
        canvas.drawText(
            "SCORE : ${player.score}",
            ((canvas.width / 2) - 100).toFloat(),
            (canvas.height - 200).toFloat(),
            whitePaint
        )
    }
    private fun draw_wave(canvas: Canvas) {
        // dessine le numéro de la vague
        canvas.drawText(
            "WAVE : ${wave}",
            ((canvas.width / 2) - 100).toFloat(),
            (canvas.height - 280).toFloat(),
            whitePaint
        )
    }

    fun reset() {
        // réinitialise les attribues de DrawingView pour lancer une nouvelle partie
        map.reset()
        player.reset()
        Monsters = CopyOnWriteArrayList<Monster>()
        Towers = CopyOnWriteArrayList<Tower>()
        Upgrade_Towers = CopyOnWriteArrayList<Tower>()
        tower_type = 2
        upgrade = false
        wave = 0
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

