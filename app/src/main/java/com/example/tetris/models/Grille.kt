package com.example.tetris.models

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Grille(
//    var taille: String,
    var height : Int,
    var width : Int,

) {
    var cases: MutableList<MutableList<Bloc?>> = MutableList(height) {MutableList(width) {null} }
    fun update(figure : Figure){
//        cases[figure.coordonnees.posy][figure.coordonnees.posx] = Bloc(Color.RED)
        for (i in 0 until figure.hitBox) {
            for (j in 0 until figure.hitBox) {
                if (figure.blocs[i][j] != null) {
                    cases[figure.coordonnees.posy + i][figure.coordonnees.posx + j] =
                        Bloc(Color.RED)
                }
            }
        }
    }

    fun draw(canvas : Canvas) {
        for (i in 0 until height){
            for (j in 0 until width){
                if(cases[i][j] != null){
                    val paint = Paint()
                    paint.color = cases[i][j]!!.color
                    canvas.drawRect(0F + (j*100), 0F +(i*100), 100F +(j*100), 100F + (i*100), paint);
                }
            }
        }
    }
}