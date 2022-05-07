package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.service.autofill.OnClickAction
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Pause_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Pause_fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pause_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btn_resume = view.findViewById<Button>(R.id.btn_resuem)
        val btn_newgame = view.findViewById<Button>(R.id.btn_newgame)
        btn_resume.setOnClickListener { OnClickR() }
        btn_newgame.setOnClickListener { onClickN() }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Pause_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            Pause_fragment().apply {

            }
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