package com.example.myapplication

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

var TotalTime = arrayOf(0,0)
class MainActivity : AppCompatActivity(),Price {
    lateinit var drawingView: DrawingView
    lateinit var time: Time
    private lateinit var monster_manager: Monster_Manager
    private lateinit var tower_manager: Tower_Manager
    private var playing = true
    var player = Player()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.MainView)
        time = Time(SystemClock.elapsedRealtime())
        monster_manager = Monster_Manager(drawingView)
        tower_manager = Tower_Manager(drawingView)
        val btn_tower = findViewById<Button>(R.id.button)
        val btn_upgrade = findViewById<Button>(R.id.button2)
        val btn_pause = findViewById<Button>(R.id.button3)
        btn_tower.setOnClickListener { OnClick(drawingView,btn_tower) }
        btn_upgrade.setOnClickListener { OnClick2(drawingView,btn_upgrade) }
        btn_pause.setOnClickListener { OnClick3()}
    }

    override fun onPause() {
        super.onPause()
        drawingView.pause()
        time.pause()
        monster_manager.pause()
        tower_manager.pause()
    }

    override fun onResume() {
        super.onResume()
        drawingView.resume()
        time.resume()
        monster_manager.resume()
        tower_manager.resume()
    }
    /*laying = false
        this.onPause()
        showGameOverDialog2("Tower Defence Révolutionnaire","Bienvenue dans notre tower defence.Nous espérons que vous prendrez du plaisir à jouer à notre création","Play",)

        drawingView.start_MonsterManager = true
        drawingView.start_TowerManager = true
        this.playing = true

    }*/
    fun gameover(){
        // affiche le fragment de fin de jeu
        onPause()
        showGameOverDialog(R.string.lose)
        /*supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, gameover_Fragment(drawingView.player.score))
            commit()*/

    }
    fun showGameOverDialog(messageId: Int) {
        class GameResult: DialogFragment() {
            override fun onCreateDialog(bundle: Bundle?): Dialog {
                val builder = AlertDialog.Builder(getActivity())
                builder.setTitle(resources.getString(messageId))
                builder.setMessage("Score : ${player.score}")
                builder.setPositiveButton("Redémarrer le jeu",
                    DialogInterface.OnClickListener { _, _-> new_game()}
                )
                return builder.create()
            }
        }

        drawingView.activity.runOnUiThread(
            Runnable {
                val ft = drawingView.activity.supportFragmentManager.beginTransaction()
                val prev =
                    drawingView.activity.supportFragmentManager.findFragmentByTag("dialog")
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
    /*fun showGameOverDialog2(Title: String, Text: String, button_text: String, function: Unit) {
        class GameResult: DialogFragment() {
            override fun onCreateDialog(bundle: Bundle?): Dialog {
                val builder = AlertDialog.Builder(getActivity())
                builder.setTitle(Title)
                builder.setMessage(Text)
                builder.setPositiveButton(button_text,
                    DialogInterface.OnClickListener { _, _-> function }
                )
                return builder.create()
            }
        }

        drawingView.activity.runOnUiThread(
            Runnable {
                val ft = drawingView.activity.supportFragmentManager.beginTransaction()
                val prev =
                    drawingView.activity.supportFragmentManager.findFragmentByTag("dialog")
                if (prev != null) {
                    ft.remove(prev)
                }
                ft.addToBackStack(null)
                val gameResult = GameResult()
                gameResult.setCancelable(false)
                gameResult.show(ft,"dialog")
            }
        )
    }*/

    fun new_game(){
        // lance une nouvelle partie
        onPause()
        drawingView.reset()
        time.reset()
        monster_manager.reset()
        onResume()
        playing = true
    }

    fun resume(){
        onResume()
        playing = true
    }

    fun OnClick(drawingView: DrawingView, button: Button) {
        // change le type de tour qui sera créée
        drawingView.tower_type+=1
        when(drawingView.tower_type){
            3->{button.text="MoneyTower:${get_price(3)}"}
            4->{button.text="IceTower:${get_price(4)}"}
            5->{drawingView.tower_type=5;button.text="SacrificeTower:${get_price(5)}"}
            6->{drawingView.tower_type=2;button.text="AttackTower:${get_price(2)}"}
        }
    }

    fun OnClick2(drawingView: DrawingView,button:Button) {
        // active/désactive upgrade
        drawingView.upgrade = !drawingView.upgrade
        button.text = "Upgrade = ${drawingView.upgrade}"
    }

    fun OnClick3() {
        // affiche le fragment de pause
        if (playing) {
            this.playing = false
            this.onPause()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment, Pause_fragment())
                commit()
            }
        }
    }
}