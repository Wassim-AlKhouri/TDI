package com.example.myapplication

class Monster {
    fun lose_HP(){}
}
fun main(){
    val matrix: Array<IntArray> = Array(11) { IntArray(9) }
    val map: Array<IntArray> = map_generator(matrix)
    for (i in map){
        for (j in i){
            print(j)
        }
        print("\n")
    }
}

fun map_generator(matrix: Array<IntArray>):Array<IntArray> {
    val height:Int = matrix.size
    val width:Int = matrix[0].size
    var map:Array<IntArray> = Array(height) { IntArray(width) }
    var x:Int = (0 until width).random()
    var y:Int = 0
    map[y][x] = 1
    var left:Boolean = true
    var right:Boolean = true
    while (y < (height-1)){
        val r = (1..3).random()
        if (r==1){
            y+=1
            left = true
            right = true
        }
        if (r == 2 && right && x<(width-1) ){
            x+=1
            left = false
        }
        if(r == 3 && left && x > 0){
            x-=1
            right = false
        }
        map[y][x] = 1
    }
    return map
}