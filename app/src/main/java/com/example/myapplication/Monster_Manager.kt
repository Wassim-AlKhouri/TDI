package com.example.myapplication

import android.graphics.Canvas
import android.os.SystemClock
import java.util.concurrent.CopyOnWriteArrayList

class Monster_Manager(val view:DrawingView):Runnable {
    lateinit var thread: Thread
    private var playing = true
    private val fibo_series = ArrayList<Int>()
    private var Last_time :Long = 0 // le temps depuis la création du dernier monstre
    private var Birth_time:Long = SystemClock.elapsedRealtime() // le temps de création du Monster_Manager
    private var monsters_created = 0 // nombre de monstres créés


    init {
        this.fibonacci() // crée la liste de fibbonacci
        Last_time = SystemClock.elapsedRealtime()
    }

    override fun run() {
        while (playing) {
            create_monsters()
            manage_monsters()
        }
    }

    private fun create_monsters() {
        val ran_monster = (1..100).random()  // le type monstre qui sera crée est choisi au hasard
        if (monsters_created <= fibo_series[view.wave]) {
            // teste si le nombre de monstres créés ne dépasse pas le nombre de monstre qui doivent être créés pendant cette vague
            if ((SystemClock.elapsedRealtime() - Last_time) >= (300..1250).random() && (SystemClock.elapsedRealtime() - Birth_time)>5000) {
                // crée des monstres à des intervales aléatoires qui vont de 0.3 à 1.25 secondes et teste si ça fait plus que 5 secondes que le jeu est lancé
                Last_time = SystemClock.elapsedRealtime()
                monsters_created += 1
                when {
                    // selon la vague où on se trouve les ratios des types de monstres crées changent
                    view.wave < 8 -> {
                        when (ran_monster) {
                            in (1..60) -> view.Monsters.add(Normal_Monster(view, view.wave))
                            in (61..89) -> if (view.wave > 3) { view.Monsters.add(Immune_Monster(view, view.wave)) } else { view.Monsters.add(Normal_Monster(view,view.wave)) }
                            in (62..100) -> if (view.wave > 5) { view.Monsters.add(Explosif_Monster(view, view.wave)) } else { view.Monsters.add(Normal_Monster(view, view.wave)) }
                        }
                    }
                    (view.wave in 8..10) -> {
                        view.player.money += 50
                        when (ran_monster) {
                            in (1..49) -> view.Monsters.add(Normal_Monster(view, view.wave))
                            in (50..75) ->  view.Monsters.add(Immune_Monster(view, view.wave))
                            in (76..100) ->  view.Monsters.add(Explosif_Monster(view, view.wave))
                        }
                    }
                    view.wave >= 10 -> {
                        view.player.money+=100
                        when (ran_monster) {
                            in (1..33) -> view.Monsters.add(Normal_Monster(view, view.wave))
                            in (34..67) -> view.Monsters.add(Immune_Monster(view, view.wave))
                            in (68..100) -> view.Monsters.add(Explosif_Monster(view, view.wave))
                        }
                    }
                }
            }
        }
        else if (monsters_created > fibo_series[view.wave] && (SystemClock.elapsedRealtime() - Last_time) > 15000 && view.Monsters.size == 0) {
            // teste si un nombre suffisant de monstre a été créés, si ça fait 15 seconde depuis la création du dérnièr monstre et si tout les monstres sont morts
            monsters_created = 0
            view.wave += 1
            Last_time = SystemClock.elapsedRealtime()
        }
    }

    private fun manage_monsters(){
        for (monster in view.Monsters) {
            // fait avancer les monstre
            monster.move()
            for (tower in view.Towers) {
                // le monstre dit à toutes les tours de tester si est assez proche d'eux
                if(tower is Attack_Tower){tower.detect_monster(monster)}
            }
        }
    }

    fun resume(){
        playing = true
        thread = Thread(this)
        thread.start()
        for (monster in view.Monsters){
            monster.LastMouvement = SystemClock.elapsedRealtime()
        }
    }

    fun pause() {
        playing = false
        thread.join()
    }

    fun reset(){
        Last_time = SystemClock.elapsedRealtime()
        monsters_created = 0
    }

    private fun fibonacci(){
        //crée la liste de fibonacci
        var t1 = 1
        var t2 = 2
        fibo_series.add(t1)
        for (i in 0..100){
            var sum = t1+t2
            t1 = t2
            t2 = sum
            fibo_series.add(t1)
        }
    }

}