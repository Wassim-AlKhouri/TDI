package com.example.myapplication

import android.graphics.Canvas
import android.util.Log
import java.util.ArrayList
import kotlin.math.min

class Map(val Col: Int, val Li: Int, var view: DrawingView)  {
    var map: Array<IntArray> = Array(Li) { IntArray(Col) }
    var Step = view.Step
    var Cells = ArrayList<Cell>()
    var road =ArrayList<Array<Int>>()

    init {
        map_generate()
    }

    fun creat_Cells(){
        for (y in 0 until Li){
            for(x in 0 until Col){
                val cell = Cell(x,y,this.Step,this.map[y][x])
                this.Cells.add(cell)
            }
        }
    }

    fun map_generate() {
        val height: Int = this.map.size
        val width: Int = this.map[0].size
        var x: Int = 3
        var y: Int = 0
        this.map[y][x] = 1
        this.road.add(arrayOf(x,y))
        var left: Boolean = true
        var right: Boolean = true
        var r =  (1..3).random()
        while (y < (height - 1)) {
            if (r == 1) {
                y += 1
                left = true
                right = true
            }
            else if (r == 2 && right && x < (width - 1)) {
                x += 1
                left = false
            }
            else if (r == 3 && left && x > 0) {
                x -= 1
                right = false
            }
            else{y+=1}
            when(r){
                1->r=(1..3).random()
                2->r=(1..2).random()
                3->r= arrayOf(1,3).random()
            }
            this.map[y][x] = 1
            this.road.add(arrayOf(x,y))
        }
    }

    fun draw(canvas: Canvas){
        for(Cell in this.Cells){
            Cell.draw(canvas)
        }
    }
}