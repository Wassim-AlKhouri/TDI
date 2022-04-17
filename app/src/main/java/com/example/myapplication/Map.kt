package com.example.myapplication

import android.graphics.Canvas
import java.util.ArrayList
import kotlin.math.min

class Map(val Col: Int, val Li: Int)  {
    var map: Array<IntArray> = Array(Li) { IntArray(Col) }
    var Step = 0F
    var Cells = ArrayList<Cell>()

    init {
        creat_Cells()
        map_generate()
    }

    fun creat_Cells(){
        for (y in 0..Col){
            for(x in 0..Li){
                val cell = Cell(x,y,Step)
                this.Cells.add(cell)
            }
        }
    }

    fun map_generate() {
        val height: Int = this.map.size
        val width: Int = this.map[0].size
        var x: Int = (0 until width).random()
        var y: Int = 0
        this.map[y][x] = 1
        var left: Boolean = true
        var right: Boolean = true
        while (y < (height - 1)) {
            val r = (1..3).random()
            if (r == 1) {
                y += 1
                left = true
                right = true
            }
            if (r == 2 && right && x < (width - 1)) {
                x += 1
                left = false
            }
            if (r == 3 && left && x > 0) {
                x -= 1
                right = false
            }
            this.map[y][x] = 1
        }
    }

    fun draw(canvas: Canvas){
        for(Cell in this.Cells){
            Cell.draw(canvas)
        }
    }
}