package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView


class gameover_Fragment(val score:Int, val wave:Int) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gameover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btn_newgame2 = view.findViewById<Button>(R.id.btn_newgame2)
        val textView =  view.findViewById<TextView>(R.id.textView3)
        textView.text = "Score:${score} \n Wave:${wave} "
        btn_newgame2.setOnClickListener { onClickN2() }
    }

    fun onClickN2(){
        // lorsqu'on appuie sur le bouton new game le fragment s'efface et lance une nouvelle partie
        (activity as MainActivity).supportFragmentManager.beginTransaction().remove(this).commit()
        (activity as MainActivity).new_game()
    }
}