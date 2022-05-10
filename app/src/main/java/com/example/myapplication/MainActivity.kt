package com.example.myapplication

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
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
    lateinit var btn_tower :Button
    lateinit var btn_upgrade :Button
    lateinit var btn_pause :Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.MainView)
        loadData()
        time = Time(SystemClock.elapsedRealtime())
        monster_manager = Monster_Manager(drawingView)
        tower_manager = Tower_Manager(drawingView)
        btn_tower = findViewById<Button>(R.id.button)
        btn_upgrade = findViewById<Button>(R.id.button2)
        btn_pause = findViewById<Button>(R.id.button3)
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

    fun gameover() {
        // affiche le fragment de fin de jeu
        onPause()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, gameover_Fragment(drawingView.player.score, drawingView.wave, drawingView.High_Score))
            commit()
        }
    }

    fun new_game(){
        // lance une nouvelle partie
        onPause()
        drawingView.reset()
        time.reset()
        monster_manager.reset()
        btn_tower.text = "AttackTower:50"
        btn_upgrade.text = "Construction Mode"
        saveData()
        onResume()
    }

    fun Show_info(Title: String, Message: String){
        //affiche les fragements d'informations
        this.onPause()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, Notice_fragment(Title, Message))
            commit()}

    }

    private fun saveData(){
        // permet de sauvegarder le plus haut score entre toutes les parties
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply{
            putInt("High Score", drawingView.High_Score)
        }.apply()
    }

    private fun loadData(){
        // permet de retrouver le meilleur score
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val savedHighScore = sharedPreferences.getInt("High Score", 0)
        drawingView.High_Score = savedHighScore
    }

    fun resume(){
        onResume()
    }

    fun OnClick(drawingView: DrawingView, button: Button) {
        // change le type de tour qui sera créée
        drawingView.tower_type+=1
        when(drawingView.tower_type){
            3->{button.text="MoneyTower:${get_price(3)}"}
            4->{button.text="IceTower:${get_price(4)}"}
            5->{drawingView.tower_type=5;button.text="UpgradeTower:${get_price(5)}"}
            6->{drawingView.tower_type=2;button.text="AttackTower:${get_price(2)}"}
        }
    }

    fun OnClick2(drawingView: DrawingView,button:Button) {
        // change de mode (construction et amélioration)
        drawingView.upgrade = !drawingView.upgrade
        if(drawingView.upgrade) {
            button.text = "Upgrade Mode"
        }
        else{
            button.text = "Construction Mode"
        }
    }

    fun OnClick3() {
        // affiche le fragment de pause
        this.onPause()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, Pause_fragment())
            commit()
        }
    }
}