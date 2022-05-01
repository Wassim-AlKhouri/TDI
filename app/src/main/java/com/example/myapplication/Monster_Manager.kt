package com.example.myapplication

class Monster_Manager(monsterCreator: Monster_Creator,val view: DrawingView):Runnable {
    lateinit var thread: Thread
    var playing = true

    override fun run() { while(true){manage_monsters()} }

    fun manage_monsters(){
        if(view.Monsters.size != 0) {
            for (monster in view.Monsters) {
                monster.move()
                for (tower in view.Towers) {
                    tower.detect_monster(monster)
                }
            }
        }
    }

    fun resume(){
        playing = true
        thread = Thread(this)
        thread.start()
    }

    fun pause() {
        playing = false
        thread.join()
    }

}