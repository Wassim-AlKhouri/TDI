package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.SystemClock
import java.util.ArrayList

abstract class Monster(open var view: DrawingView, wave: Int) {

    abstract val road : ArrayList<Array<Int>>
    abstract val Step :Float
    abstract var health : Int
    abstract val value:Int
    var paint = Paint()
    var x = 0f
    var y = 0f
    var r =RectF(0f,0f,0f,0f)
    val radius = 40f
    var speed = 0.08f
    var pos = 0 // la case du chemin où se trouve le monstre
    var dead = false
    var iced = false // si le monstre est gelé
    var iced_time:Long = 0 // le temps où le monstre a été gelé
    var d = 0f // distance parcouru par le monstre(expliqué dans move)
    var ran = 0 // ran correspond au décalage du monstre par rapport au centre du chemin
    var LastMouvement = SystemClock.elapsedRealtime() // temps depuis la dérnière mouvment du monstre

    init {
        val r = ((Step/2)-radius).toInt()
        this.ran = (r..-r).random()
    }

    abstract fun special_move() // dedans on définira la couleur du monstre et ce qui le rend spéciale
    abstract fun attacked(damage:Int,ice:Boolean)

    fun draw(canvas: Canvas){
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
            si d est plus grand que la côté de la case alors on place le monstre sur la prochaine case du chemin
            et on retranche la côté de "d"
            */
            d-=Step
            pos+=1
        }
        if (iced) { if ((SystemClock.elapsedRealtime() - iced_time) <= 2000) { this.speed=0.03f } }
        if (pos < (road.size - 1)) {
            // test si on est pas à la fin du chemin
            x = ((road[pos][0] + 0.5)*Step).toFloat()
            y = ((road[pos][1] + 0.5)*Step).toFloat()
            /*
            on commence par placer le monstre au milieu de la case du chemin où il se trouve
            puis on ajout/enlève "d" de "x" ou "y" par rapport à la prochaine case de chemin
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
            // test si on est à la fin du chemin, si oui on attaque le joueur et le monstre meurt
            view.player.lose_healthpoints()
            this.dead = true
            view.Monsters.remove(this) // le monstre s'enlève de la liste des monstres
        }
    }

    fun get_pos(tf:Int): Array<Float> {
        // donne la position du monstre à un certain temps donnée
        var local_d = this.d + (SystemClock.elapsedRealtime()+tf - this.LastMouvement)*speed
        var local_pos = pos
        var local_x = 0f
        var local_y = 0f
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
    override var health = 200 + (wave*25)
    override val value = 10
    init {
        x=( (road[0][0]+0.5)*Step ).toFloat()
        val r = ((Step/2)-radius).toInt()
        this.ran = (-r..r).random()
    }

    override fun special_move() {paint.color = Color.BLACK} // le monstre n'a rien de spéciale

    override fun attacked(damage: Int, ice: Boolean) {
        health -= damage
        if (health <= 0) {
            dead = true
            view.player.score+=value
            view.player.money+=value + 10*wave
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
    override var health = 300 + (wave*25)
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
                view.player.money+=value + 10*wave
            }
        }
    }

}

class Explosif_Monster(override var view: DrawingView, val wave: Int):Monster(view, wave){
    // monstres explosives qui après un certain temps explosent et détruit une tour en mourant
    override val road = view.map.road
    override val Step = view.Step
    override val value = 40
    override var health = 100 + (wave*25)
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
            view.player.money+=value + 10*wave
        }
        if (ice){iced=true;iced_time=SystemClock.elapsedRealtime()}
    }

    override fun special_move() {
        paint.color = Color.RED
        if((SystemClock.elapsedRealtime()- birth_time) >= 5000){
            // test si le monstre est resté en vie pendant plus que 5 secondes
            if(view.Sacrifice_Towers.size !=0){
                // si il existe des tours de sacrifice alors le monstre en attaque une
                val tower = view.Sacrifice_Towers.random()
                tower.explode()
                this.dead = true
                view.Monsters.remove(this)
            }
            else if(view.Towers.size !=0) {
                // si il n'existe pas de tour de sacrifice alros le monstre attque une tours au hasard
                val tower = view.Towers.random()
                tower.explode()
                this.dead = true
                view.Monsters.remove(this)
            }

        }
    }

}