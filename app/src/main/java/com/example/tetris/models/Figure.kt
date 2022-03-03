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

    //La figure courante
    lateinit var blocs: Array<Array<Bloc?>>

    // Les rotation possible de la figure (Peux avoir mois de 4 roation possibe, comme le Baton)
    lateinit var rotate0: Array<Array<Bloc?>>
    lateinit var rotate1: Array<Array<Bloc?>>
    lateinit var rotate2: Array<Array<Bloc?>>
    lateinit var rotate3: Array<Array<Bloc?>>

    // L'id de la roation courrante (0 pour rotate0, 1 pour rotate1 ect...)
    var currentRotate : Int = 0


    // BUG -> Si la figure et a droite de l'ecran et qu'on rotate, crash
    // BUG -> Si on rotate a coté d'une figure, pb de colision
    override fun rotate(sens: EnumSens) {
        if (sens == EnumSens.SENS_HORAIRE){
            currentRotate = (currentRotate+1)%nbRotate
        }
        else {
            currentRotate = (currentRotate-1)%nbRotate
        }
        when(currentRotate){
            0 -> blocs = rotate0
            1-> blocs = rotate1
            2-> blocs = rotate2
            3-> blocs = rotate3
        }
    }

    // Si la figure a touché le sol (bas d'ecran ou une autre figure), à revoir c'est très experimental
    fun hasItGround(grille : Grille): Boolean {
        // touche le sol
        if(coordonnees.posy >= grille.height - hitBox){
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
            if (grille.cases[it.posy + 1][it.posx] != null){
                return true
            }
        }
        return false
    }

    // Si la figure n'a pas d'obstacle a sa droite
    private fun hasNoFigureInRight(grille : Grille) : Boolean{
        if(coordonnees.posx >= grille.width ){
            return false
        }
        val saveCoord : MutableList<Coordonnees> = mutableListOf()
        for (i in 0 until hitBox) {
            for (j in 0 until hitBox) {
                if(blocs[i][j] != null){
                    if (j == hitBox-1){
                        saveCoord.add(Coordonnees(coordonnees.posx + j, coordonnees.posy + i))
                    } else if (blocs[i][j+1] == null){
                        saveCoord.add(Coordonnees(coordonnees.posx + j, coordonnees.posy + i))
                    }
                }
            }
        }
        saveCoord.forEach {
            if (grille.cases[it.posy][it.posx + 1] != null){
                return false
            }
        }
        return true
    }

    // Si la figure n'a pas d'obstacle a sa gauche
    private fun hasNoFigureInLeft(grille : Grille) : Boolean{
        if(coordonnees.posx <= 0 ){
            return false
        }
        val saveCoord : MutableList<Coordonnees> = mutableListOf()
        for (i in 0 until hitBox) {
            for (j in 0 until hitBox) {
                if(blocs[i][j] != null){
                    if (j == 0){
                        saveCoord.add(Coordonnees(coordonnees.posx + j, coordonnees.posy + i))
                    } else if (blocs[i][j-1] == null){
                        saveCoord.add(Coordonnees(coordonnees.posx + j, coordonnees.posy + i))
                    }
                }
            }
        }
        saveCoord.forEach {
            if (grille.cases[it.posy][it.posx - 1] != null){
                return false
            }
        }
        return true
    }

    // Modifie les coordonée de la figure et ajoutant de 1 les coordonées y et en prenant en compte les
    // valeurs de l'acceléromètre
    fun updateCoord(valuesAcceleromoetre : MutableList<Float>, grille : Grille){
        coordonnees.posy += 1
        if (valuesAcceleromoetre[0] > 0.5 ){
            if(coordonnees.posx > 0 && hasNoFigureInLeft(grille))
                coordonnees.posx--
        }
        if (valuesAcceleromoetre[0] < -0.5 ){
            if(coordonnees.posx < grille.width-1 && hasNoFigureInRight(grille))
            coordonnees.posx++
        }
    }

    // Dessine la figure
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

