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


    // Rotation d'une figure en choisissant le sens
//    override fun rotate(sens: EnumSens) { // fonction rotate tableau matrice carre
//        val newBlocs: Array<Array<Bloc?>> = Array(hitBox) { Array(hitBox) { null } }
//        for (i in 0 until hitBox-1){
//            for (j in 0 until hitBox-1){
//                val newCord = this.changeCoordBloc(Coordonnees(i, j), sens)
//                if(blocs[i][j] != null ) {
//                    newBlocs[newCord.posx][newCord.posy] = Bloc(color)
//                }
//            }
//        }
//        blocs = newBlocs
//    }

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

    // Fonction pas du tout optimisée, mais par rapport a notre porjet, je pense pas qu'on
    // ai besoin de plus
    private fun changeCoordBloc(coord : Coordonnees, sens: EnumSens) : Coordonnees {
        if (sens == EnumSens.SENS_HORAIRE){
            when (coord) {
                Coordonnees(0,0) -> return Coordonnees(0,2)
                Coordonnees(0,2) -> return Coordonnees(2,2)
                Coordonnees(2,2) -> return Coordonnees(2,0)
                Coordonnees(2,0) -> return Coordonnees(0,0)
                Coordonnees(0,1) -> return Coordonnees(1,2)
                Coordonnees(1,2) -> return Coordonnees(2,1)
                Coordonnees(2,1) -> return Coordonnees(1,0)
                Coordonnees(1,0) -> return Coordonnees(0,1)
                else -> return Coordonnees(1,1)
            }
        } else {
            when (coord) {
                Coordonnees(0,0) -> return Coordonnees(2,0)
                Coordonnees(0,2) -> return Coordonnees(0,0)
                Coordonnees(2,2) -> return Coordonnees(0,2)
                Coordonnees(2,0) -> return Coordonnees(2,2)
                Coordonnees(0,1) -> return Coordonnees(1,0)
                Coordonnees(1,2) -> return Coordonnees(0,1)
                Coordonnees(2,1) -> return Coordonnees(1,2)
                Coordonnees(1,0) -> return Coordonnees(2,1)
                else -> return Coordonnees(1,1)
            }
        }
    }

    fun updateCoord(){
        coordonnees.posy += 1
    }

    // Si la fihure a touché le bas de l'ecran, a revoir c'est très experimental
    fun hasItGround(canvas: Canvas?): Boolean {
        if (canvas != null) {
            return coordonnees.posy * 100 >= canvas.height - hitBox*100
        }
        return false
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
                        paint.setColor(blocs[i][j]!!.color)
                        canvas.drawRect(0F + (j*100) + (coordonnees.posx*100), 0F +(i*100) + (coordonnees.posy*100), 100F +(j*100) + (coordonnees.posx*100), 100F + (i*100)  + (coordonnees.posy*100), paint);
                    }
                }
            }
        }
    }

}

