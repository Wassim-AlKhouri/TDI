package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.SystemClock
import java.util.ArrayList

abstract class Monster(open var view: DrawingView, wave: Int) {

    protected abstract val road : ArrayList<Array<Int>>
    protected abstract val Step :Float
    protected abstract var health : Int
    protected abstract val value:Int
    protected var paint = Paint()
    var x = 0f
    var y = 0f
    var r =RectF(0f,0f,0f,0f)
    protected val radius = 40f
    private var speed = 0.08f
    private var pos = 0 // la case du chemin où se trouve le monstre
    var dead = false
    protected var iced = false // si le monstre est gelé
    protected var iced_time:Long = 0 // le temps où le monstre a été gelé
    protected var d = 0f // distance parcouru par le monstre(expliqué dans move)
    protected var ran = 0 // ran correspond au décalage du monstre par rapport au centre du chemin
    var LastMouvement = SystemClock.elapsedRealtime() // temps du dernier mouvment du monstre

    init {
        val r = ((Step/2)-radius).toInt()
        this.ran = (r..-r).random()
    }

    abstract fun special_move() // on définira la couleur du monstre et ce qui le rend spécial
    abstract fun attacked(damage:Int,ice:Boolean) // le monstre est attaqué

    fun draw(canvas: Canvas){
        // dessine le monstre
        if (!dead) {
                canvas.drawRect(r,paint)
            }
    }

    fun move(){
        special_move()
        this.d += (SystemClock.elapsedRealtime() - this.LastMouvement)*speed // on ajoute la distance parcouru depuis le dernier mouvement
        this.LastMouvement = SystemClock.elapsedRealtime() // on redéfinit le temps du dernier mouvement
        if (d>=Step){
            /*
            si d est plus grand que la longueur du côté de la case alors on place le monstre sur la prochaine case du chemin
            et on retranche cette longueur de "d"
            */
            d-=Step
            pos+=1
        }
        if (iced) { if ((SystemClock.elapsedRealtime() - iced_time) <= 2000) { this.speed=0.03f } }
        if (pos < (road.size - 1)) {
            // teste si on est pas à la fin du chemin
            x = ((road[pos][0] + 0.5)*Step).toFloat()
            y = ((road[pos][1] + 0.5)*Step).toFloat()
            /*
            on commence par placer le monstre au milieu de la case du chemin où il se trouve
            puis on ajoute/enlève "d" de "x" ou "y" par rapport à la prochaine case du chemin
            */
            if (road[pos][0] - road[pos+1][0] > 0) {
                x -= d
            } else if (road[pos][0] - road[pos+1][0] < 0) {
                x += d
            } else if (road[pos][1] - road[pos+1][1] < 0) {
                y += d
            }
            this.r = RectF(x-(radius/2)+ran,y-(radius/2)+ran,x+(radius/2)+ran,y+(radius/2)+ran)
        }
        if(pos == (road.size - 1)){
            // teste si on est à la fin du chemin, si oui on attaque le joueur et le monstre meurt
            view.player.lose_healthpoints()
            this.dead = true
            view.Monsters.remove(this) // le monstre s'enlève de la liste des monstres
        }
    }

    fun get_pos(tf:Int): Array<Float> {
        // prédit la position du monstre à un certain temps donné
        var local_d = this.d + (SystemClock.elapsedRealtime()+tf - this.LastMouvement)*speed
        var local_pos = pos
        var local_x: Float
        var local_y: Float
        if (local_d>=Step){
            local_d-=Step
            local_pos+=1
        }
        if(local_pos < (road.size - 1)) {
            local_x = ((road[local_pos][0] + 0.5)*Step).toFloat() + ran
            local_y = ((road[local_pos][1] + 0.5)*Step).toFloat() + ran
            if (road[local_pos][0] - road[local_pos + 1][0] > 0) {
                local_x -= local_d
            } else if (road[local_pos][0] - road[local_pos + 1][0] < 0) {
                local_x += local_d
            } else if (road[local_pos][1] - road[local_pos + 1][1] < 0) {
                local_y += local_d
            }
        }
        else{
            local_pos = road.size - 1
            local_x = ((road[local_pos][0] + 0.5)*Step).toFloat() + ran
            local_y = ((road[local_pos][1] + 0.5)*Step).toFloat() + ran
        }
        return(arrayOf(local_x,local_y))
    }
}

class Normal_Monster(override var view: DrawingView, val wave: Int):Monster(view, wave){
    // monstres normaux
    override val road = view.map.road
    override val Step = view.Step
    override var health = 200 + (wave*80)
    override val value = 10
    init {
        x=( (road[0][0]+0.5)*Step ).toFloat()
        val r = ((Step/2)-radius).toInt()
        this.ran = (-r..r).random()
    }

    override fun special_move() {paint.color = Color.BLACK} // le monstre n'a rien de spécial

    override fun attacked(damage: Int, ice: Boolean) {
        health -= damage
        if (health <= 0) {
            dead = true
            view.player.score+=value
            view.player.money+=value + wave/2
            view.Monsters.remove(this)
        }

        if (ice){
            // si le projectile est de type glace alors le monstre est gelé
            iced=true
            iced_time=SystemClock.elapsedRealtime()
        }
    }

}

class Immune_Monster(override var view: DrawingView,val wave: Int):Monster(view, wave){
    // monstres immunisés qui ne prennent pas de dégâts pendant un certain temps et ne peuvent pas être gelés
    override val road = view.map.road
    override val Step = view.Step
    override var health = 300 + (wave*80)
    override val value = 30
    private var immune:Boolean = false
    init {
        x=( (road[0][0]+0.5)*Step ).toFloat()
        val r = ((Step/2)-radius).toInt()
        this.ran = (-r..r).random()
    }

    override fun special_move() {
        paint.color = Color.BLACK
        // le monstre ne prend pas de dégât une seconde sur deux
        immune = (TotalTime[1]).mod(2) == 0
        if(immune){paint.color = Color.GRAY}
    }

    override fun attacked(damage: Int, ice: Boolean) {
        if(!immune){
            health -= damage
            if (health <= 0){
                dead = true
                view.player.score+=value
                view.Monsters.remove(this)
                view.player.money+=value + wave/2
            }
        }
    }
}

class Explosif_Monster(override var view: DrawingView, val wave: Int):Monster(view, wave){
    // monstres explosifs qui après un certain temps explosent et détruit une tour en mourant
    override val road = view.map.road
    override val Step = view.Step
    override val value = 40
    override var health = 100 + (wave*80)
    private val birth_time = LastMouvement

    init {
        x=( (road[0][0]+0.5)*Step ).toFloat()
        val r = ((Step/2)-radius).toInt()
        this.ran = (-r..r).random()
    }

    override fun attacked(damage: Int, ice: Boolean) {
        health -= damage
        if (health <= 0){
            dead = true
            view.player.score+=value
            view.Monsters.remove(this)
            view.player.money+=value + wave/2
        }
        if (ice){iced=true;iced_time=SystemClock.elapsedRealtime()}
    }

    override fun special_move() {
        paint.color = Color.RED
        if((SystemClock.elapsedRealtime()- birth_time) >= 5000){
            // teste si le monstre est resté en vie pendant plus de 5 secondes
            if(view.Upgrade_Towers.size !=0){
                // si il existe des tours d'amélioration alors le monstre en attaque une
                val tower = view.Upgrade_Towers.random()
                tower.explode()
                this.dead = true
                view.Monsters.remove(this)
            }
            else if(view.Towers.size !=0) {
                // si il n'y a pas de tour d'amélioration alors le monstre attaque une tour au hasard
                val tower = view.Towers.random()
                tower.explode()
                this.dead = true
                view.Monsters.remove(this)
            }

        }
    }

}