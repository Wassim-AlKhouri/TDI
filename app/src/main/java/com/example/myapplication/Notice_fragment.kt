package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class Notice_fragment(val Title: String, val message : String): Fragment() {
    // fragment qui affiche divers informations concernant les monstres
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.notice_fragment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btn_continue = view.findViewById<Button>(R.id.btncontinue)
        val textViewN =  view.findViewById<TextView>(R.id.textViewN)
        val titleviewN = view.findViewById<TextView>(R.id.titreviewN)
        textViewN.text = message
        titleviewN.text = Title
        btn_continue.setOnClickListener { OnClickN() }
    }
    fun OnClickN(){
        (activity as MainActivity).supportFragmentManager.beginTransaction().remove(this).commit()
        (activity as MainActivity).resume()
    }
}