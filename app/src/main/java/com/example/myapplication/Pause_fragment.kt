package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.service.autofill.OnClickAction
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class Pause_fragment : Fragment() {
    // fragment qui apparait quand on met le jeu en pause
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pause_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btn_resume = view.findViewById<Button>(R.id.btn_resume)
        val btn_newgame = view.findViewById<Button>(R.id.btn_newgame)
        btn_resume.setOnClickListener { OnClickR() }
        btn_newgame.setOnClickListener { onClickN() }
    }

    fun OnClickR(){
        (activity as MainActivity).supportFragmentManager.beginTransaction().remove(this).commit()
        (activity as MainActivity).resume()
    }
    fun onClickN(){
        (activity as MainActivity).supportFragmentManager.beginTransaction().remove(this).commit()
        (activity as MainActivity).new_game()
    }
}