package com.example.tetris.models

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.DisplayMetrics

open class Figure(
    var nom: String,
    var coordonnees: Coordonnees,
    var color : Int,
    var hitBox : Int,
    val nbRotate : Int
) : IRotatable {

    lateinit var blocs: Array<Array<Bloc?>>
    lateinit var rotate0: Array<Array<Bloc?>>
    lateinit var rotate1: Array<Array<Bloc?>>
    lateinit var rotate2: Array<Array<Bloc?>>
    lateinit var rotate3: Array<Array<Bloc?>>
    var currentotate : Int = 0

    override fun rotate(sens: EnumSens) {
        if (sens == EnumSens.SENS_HORAIRE){
            currentotate = (currentotate+1)%nbRotate
        }
        else {
            currentotate = (currentotate-1)%nbRotate
        }
        when(currentotate){
            0 -> blocs = rotate0
            1-> blocs = rotate1
            2-> blocs = rotate2
            3-> blocs = rotate3
        }
    }

    // Si la figure a touché le bas de l'ecran, a revoir c'est très experimental
    fun hasItGround(canvas: Canvas?, grille : Grille): Boolean {
//        if (coordonnees.posy >= grille.height) {
//            return coordonnees.posy * 100 >= canvas.height - hitBox*100
//        }
        // touche le sol
        if(coordonnees.posy >= grille.height - hitBox){
            println("true1")
            return true
        }

        val saveCoord : MutableList<Coordonnees> = mutableListOf()

        for (i in 0 until hitBox) {
            for (j in 0 until hitBox) {
                if(blocs[i][j] != null){
                    if (i == hitBox - 1){
                        saveCoord.add(Coordonnees(coordonnees.posx + j, coordonnees.posy + i))
                    } else if (blocs[i+1][j] == null){
                        saveCoord.add(Coordonnees(coordonnees.posx + j, coordonnees.posy + i))
                    }
                }
            }
        }

        saveCoord.forEach {
            println(it.posx)
            println(it.posy)
            if (grille.cases[it.posy + 1][it.posx] != null){
                println("true2")
                return true
            }
        }
        return false
    }

    fun updateCoord(){
        coordonnees.posy += 1
    }

    // modifier les cordonée en ajoutant celle en paramètre
    fun updateCoord(newCoord : Coordonnees){
        coordonnees.posx += newCoord.posx
        coordonnees.posy += newCoord.posy
    }

    fun draw(canvas: Canvas?){
        val paint = Paint()

        if (canvas != null) {
            for (i in 0 until hitBox){
                for (j in 0 until hitBox){
                    if( blocs[i][j] != null){
                        paint.color = blocs[i][j]!!.color
                        canvas.drawRect(
                            0F + (j*100) + (coordonnees.posx*100),
                            0F +(i*100) + (coordonnees.posy*100),
                            100F +(j*100) + (coordonnees.posx*100),
                            100F + (i*100)  + (coordonnees.posy*100),
                            paint);
                    }
                }
            }
        }
    }

}

