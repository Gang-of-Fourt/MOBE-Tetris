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

    private fun doCopy() : Figure {
        val saveFigure = Figure(nom, coordonnees, color, hitBox, nbRotate, currentRotate)
        saveFigure.blocs = blocs.copyOf()
        saveFigure.rotate0 = rotate0.copyOf()
        saveFigure.rotate1 = rotate1.copyOf()
        saveFigure.rotate2 = rotate2.copyOf()
        saveFigure.rotate3 = rotate3.copyOf()
        return saveFigure
    }

    private fun isInObstacle2(grille : Grille) : Boolean{
        for (i in 0 until hitBox) {
            for (j in 0 until hitBox) {
                if(blocs[i][j] != null){
                    if (coordonnees.posx + j >= grille.width){
                        return true
                    }
                    else if(coordonnees.posx + j < 0){
                        return true
                    }
                    if(coordonnees.posx + j >= 0 && coordonnees.posx + j < grille.width) {
                        if (grille.cases[coordonnees.posy + i][coordonnees.posx + j] != null) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

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

                            if(nom == "Baton"){
                                if (j == 1) {
                                    leftObstacle = true
                                }
                                else if (j == 2) {
                                    rightObstacle = true
                                }
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

//            color = Color.MAGENTA
            color = Color.GREEN
            return EnumObstacle.RIGHT_AND_LEFT_OBSTACLE
        } else if (rightObstacle){
            // S'il peut se déplacer vers la gauche
            return EnumObstacle.RIGHT_OBSTACLE
            if (hasNoFigureInLeft2(grille)) {
//                color = Color.GREEN
                return EnumObstacle.RIGHT_OBSTACLE
            } else {

                color = Color.BLACK
                return EnumObstacle.RIGHT_AND_LEFT_OBSTACLE
            }
        } else if (leftObstacle){
            // S'il peut se déplacer vers la droite
            return EnumObstacle.LEFT_OBSTACLE
            if (hasNoFigureInRight2(grille)) {
//                color = Color.MAGENTA
                return EnumObstacle.LEFT_OBSTACLE
            } else {
                println("3")
//                color = Color.BLUE
                color = Color.YELLOW
                return EnumObstacle.RIGHT_AND_LEFT_OBSTACLE
            }
        } else {
//            color = Color.CYAN
            return EnumObstacle.NO_OBSTCALE
        }
    }

    //TODO BUG -> figure baton, crash quand rorate quand il est collé à droite de l'ecran en pos verticale
    override fun rotate(sens: EnumSens, grille : Grille) {

        // On save la figure courrante avec son indice de rotation au cas ou la roation ne peut finalement
        //pas avoir lieu
        println("rotate")
        val saveBlocs = blocs.copyOf()
        val saveCurrentRotate = currentRotate
        val saveCoordonneesX = coordonnees.posx

//        val saveFigure = doCopy()

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



//        val res : EnumObstacle
//        //Tans que la figure est dans une autre figure ou en dehors de la grille, on la déplace suivant si l'obstacle
//        //est à gauche ou à droite
//
//        res = isInObstacle(grille)
//        if (res == EnumObstacle.LEFT_OBSTACLE){
//            color = Color.MAGENTA
//
//            coordonnees.posx++
//        } else if (res == EnumObstacle.RIGHT_OBSTACLE) {
//            color = Color.RED
//
//            coordonnees.posx--
//            if(nom == "Baton"){
//                if(isInObstacle(grille) == EnumObstacle.RIGHT_OBSTACLE){
//                    coordonnees.posx--
//                }
//            }
//        }

        // Si le figure est concé entre 2 ovsatcle, on annule la rotation
        if (isInObstacle2(grille)){

            blocs = saveBlocs
            currentRotate = saveCurrentRotate
            coordonnees.posx = saveCoordonneesX

//            blocs = saveFigure.blocs.copyOf()
//            currentRotate = saveFigure.currentRotate
//            coordonnees.posx = saveFigure.coordonnees.posx
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


    private fun hasNoFigureInRight2(grille : Grille) : Boolean{
        coordonnees.posx++
        if (isInObstacle(grille) != EnumObstacle.NO_OBSTCALE){
            coordonnees.posx--
            return false
        }
        coordonnees.posx--
        return true
    }

    private fun hasNoFigureInLeft2(grille : Grille) : Boolean{
        coordonnees.posx--
        if (isInObstacle(grille) != EnumObstacle.NO_OBSTCALE){
            coordonnees.posx++
            return false
        }
        coordonnees.posx++
        return true
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
                    else if (grille.cases[coordonnees.posy + i][coordonnees.posx + j + 1] != null){
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
                        println("A : i = "+i+"; j = "+j)
                        return false
                    }
                    if (grille.cases[coordonnees.posy + i][coordonnees.posx + j - 1] != null){
                        println("B : i = "+i+"; j = "+j)
                        println("B : y = "+(coordonnees.posy + i)+"; x = "+(coordonnees.posx + j - 1))
                        print(grille.cases[coordonnees.posy + i][coordonnees.posx + j - 1]?.color)
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
            if(hasNoFigureInLeft2(grille))
                coordonnees.posx--
        }
        if (valuesAcceleromoetre[0] < -0.5 ){
            if(hasNoFigureInRight2(grille))
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
                        blocs[i][j]!!.color = color
                        //TODO a modifier
                        paint.color = color
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

