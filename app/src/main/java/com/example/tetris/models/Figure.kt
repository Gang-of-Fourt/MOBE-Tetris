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
    val nbRotate : Int,
    // L'id de la roation courrante (0 pour rotate0, 1 pour rotate1 ect...)
    var currentRotate : Int
) : IRotatable {

    //La figure courante
    lateinit var blocs: Array<Array<Bloc?>>

    // Les rotation possible de la figure (Peux avoir mois de 4 roation possibe, comme le Baton)
    lateinit var rotate0: Array<Array<Bloc?>>
    lateinit var rotate1: Array<Array<Bloc?>>
    lateinit var rotate2: Array<Array<Bloc?>>
    lateinit var rotate3: Array<Array<Bloc?>>

    // Vérifie si la figure est dans un obstacle (extrémité grille ou autre figure)
    // et précise si l'obstacle est a droite, a gauche ou des 2 cotés
    private fun isInObstacle(grille : Grille) : EnumObstacle{
        var leftObstacle = false
        var rightObstacle = false
        for (i in 0 until hitBox) {
            for (j in 0 until hitBox) {
                if(blocs[i][j] != null){
                    if (coordonnees.posx + j >= grille.width){
                        rightObstacle = true
                    }
                    else if(coordonnees.posx + j < 0){
                        leftObstacle = true
                    }
                    if(coordonnees.posx + j >= 0 && coordonnees.posx + j < grille.width) {
                        if (grille.cases[coordonnees.posy + i][coordonnees.posx + j] != null) {
                            if (j == 0) {
                                leftObstacle = true
                            } else if (j == hitBox - 1) {
                                rightObstacle = true
                            } else {
                                //TODO voir ce cas la plus précisement
                                rightObstacle = true
                                leftObstacle = true
                            }
                        }
                    }
                }
            }
        }
        return analyseResult(leftObstacle, rightObstacle, grille)
    }

    // Analyse le traitement de la fonction ci dessus (isInObstacle)
    private fun analyseResult(leftObstacle : Boolean, rightObstacle : Boolean, grille: Grille) : EnumObstacle{
        if(leftObstacle && rightObstacle){
            return EnumObstacle.RIGHT_AND_LEFT_OBSTACLE
        } else if (rightObstacle){
            if (hasNoFigureInLeft(grille)) {
                return EnumObstacle.RIGHT_OBSTACLE
            } else {
                return EnumObstacle.RIGHT_AND_LEFT_OBSTACLE
            }
        } else if (leftObstacle){
            if (hasNoFigureInRight(grille)) {
                return EnumObstacle.LEFT_OBSTACLE
            } else {
                return EnumObstacle.RIGHT_AND_LEFT_OBSTACLE
            }
        } else {
            return EnumObstacle.NO_OBSTCALE
        }
    }

    // BUG -> Si la figure et a droite de l'ecran et qu'on rotate, crash
    // BUG -> Si on rotate a coté d'une figure, pb de colision
    override fun rotate(sens: EnumSens, grille : Grille) {

        // On save la figure courrante avec son indice de rotation au cas ou la roation ne peut finalement
        //pas avoir lieu
        val saveBlocs = blocs.copyOf()
        val saveCurrentRotate = currentRotate

        // Change l'indice de rotation
        if (sens == EnumSens.SENS_HORAIRE){
            currentRotate = (currentRotate+1)%nbRotate
        }
        else {
            currentRotate = (currentRotate-1)%nbRotate
        }

        //Fait la rotation
        when(currentRotate){
            0 -> blocs = rotate0
            1 -> blocs = rotate1
            2 -> blocs = rotate2
            3 -> blocs = rotate3
        }

        var res : EnumObstacle
        //Tans que la figure est dans une autre figure ou en dehors de la grille, on la déplace suivant si l'obstacle
        //est à gauche ou à droite
        do {
            res = isInObstacle(grille)
            if (res == EnumObstacle.LEFT_OBSTACLE){
                coordonnees.posx++
            } else if (res == EnumObstacle.RIGHT_OBSTACLE) {
                coordonnees.posx--
            }

        } while(res != EnumObstacle.NO_OBSTCALE && res !=EnumObstacle.RIGHT_AND_LEFT_OBSTACLE)

        // Si le figure est concé entre 2 ovsatcle, on annule la rotation
        if (res == EnumObstacle.RIGHT_AND_LEFT_OBSTACLE){
            blocs = saveBlocs
            currentRotate = saveCurrentRotate
        }
    }

    // Si la figure a touché le sol (bas d'ecran ou une autre figure)
    fun hasItGround(grille : Grille): Boolean {
        // Parcour tous les blocs de la figure, pour chaque blocs, vérifie si la figure se trouve au dessus d'un obstacle
        for (i in 0 until hitBox) {
            for (j in 0 until hitBox) {
                if(blocs[i][j] != null){
                    if (coordonnees.posy + i + 1 >= grille.height){
                        return true
                    }
                    if (grille.cases[coordonnees.posy + i + 1][coordonnees.posx + j] != null){
                        return true
                    }

                }
            }
        }
        return false
    }

    // Si la figure n'a pas d'obstacle a sa droite
    private fun hasNoFigureInRight(grille : Grille) : Boolean{
        // Parcour tous les blocs de la figure, pour chaque bloc, vérifie s'il y a un obstacle à droite de la figure
        // que se soit l'extrémité droite de la grille ou un obstacle a droite
        for (i in 0 until hitBox) {
            for (j in 0 until hitBox) {
                if(blocs[i][j] != null){
                    // Si la figure se trouve  l'extrémité droite
                    if (coordonnees.posx + j + 1 >= grille.width){
                        return false
                    }
                    // S'il y a un bloc a droite de la figure
                    else if (grille.cases[coordonnees.posy + i][coordonnees.posx + j+ 1] != null){
                        return false
                    }
                }
            }
        }
        return true
    }

    // Si la figure n'a pas d'obstacle a sa gauche
    private fun hasNoFigureInLeft(grille : Grille) : Boolean{
        // Meme fonctionnement que la fonction ci-dessus (hasNoFigureInRight)
        for (i in 0 until hitBox) {
            for (j in 0 until hitBox) {
                if(blocs[i][j] != null){
                    if (coordonnees.posx + j - 1 < 0){
                        return false
                    }
                    if (grille.cases[coordonnees.posy + i][coordonnees.posx + j - 1] != null){
                        return false
                    }
                }
            }
        }
//
        return true
    }

    // Modifie les coordonée de la figure et ajoutant de 1 les coordonées y et en prenant en compte les
    // valeurs de l'acceléromètre
    fun updateCoord(valuesAcceleromoetre : MutableList<Float>, grille : Grille){
        coordonnees.posy += 1
        if (valuesAcceleromoetre[0] > 0.5 ){
            if(hasNoFigureInLeft(grille))
                coordonnees.posx--
        }
        if (valuesAcceleromoetre[0] < -0.5 ){
            if(hasNoFigureInRight(grille))
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

