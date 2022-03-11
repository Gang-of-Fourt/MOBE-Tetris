package com.example.tetris.models

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.DisplayMetrics

open class Figure(
    var nom: String,
    var coordonnees: Coordonnees,
    var color : Int,
    val hitBox : Int, // La taille de sa hitbox
    val nbRotate : Int, // Le nombre de rotation possible
    var currentRotate : Int // L'id de la roation courrante (0 pour rotate0, 1 pour rotate1 ect...)
) : IRotatable {

    //La figure courante
    lateinit var blocs: Array<Array<Bloc?>>

    // Les rotation possible de la figure (Peux avoir mois de 4 roation possibe, comme le Baton)
    lateinit var rotate0: Array<Array<Bloc?>>
    lateinit var rotate1: Array<Array<Bloc?>>
    lateinit var rotate2: Array<Array<Bloc?>>
    lateinit var rotate3: Array<Array<Bloc?>>


    fun doCopy() : Figure {
        val saveFigure = Figure(nom, coordonnees, color, hitBox, nbRotate, currentRotate)
        saveFigure.blocs = blocs.copyOf()
        return saveFigure
    }

    // Vérifie sir la figure courante est dans un obstacle (autre figure ou en dehors de la grille)
    private fun isInObstacle(grille : Grille) : Boolean{
        for (i in 0 until hitBox) {
            for (j in 0 until hitBox) {
                if(blocs[i][j] != null){

                    if (coordonnees.posx + j >= grille.width || // Si un bloc est à droite de la grille
                        coordonnees.posx + j < 0 || // Si un bloc est à gauche de la grille
                        coordonnees.posy + i >= grille.height || // Si un bloc est sous la grille
                        grille.cases[coordonnees.posy + i][coordonnees.posx + j] != null // Si un bloc est dans un autre bloc
                        )
                    {
                        return true
                    }
                }
            }
        }
        return false
    }

    // Effectue la rotation de la figure courante
    override fun rotate(sens: EnumSens, grille : Grille) {
        // On save la figure courrante avec son indice de rotation au cas ou la roation ne peut finalement
        // pas avoir lieu
        val saveFigure = doCopy()

        // Change l'indice de rotation
        if (sens == EnumSens.SENS_HORAIRE){
            currentRotate = (currentRotate+1)%nbRotate
        }
        else {
            currentRotate = if (currentRotate-1 == -1)  3%nbRotate else  (currentRotate-1)
        }

        //Fait la rotation
        when(currentRotate){
            0 -> blocs = rotate0
            1 -> blocs = rotate1
            2 -> blocs = rotate2
            3 -> blocs = rotate3
        }

        // Si le figure se trouve dans un obstacle apres sa rotation, l'annule, la rotation est donc impossible
        if (isInObstacle(grille)){
            blocs = saveFigure.blocs.copyOf()
            currentRotate = saveFigure.currentRotate
            coordonnees.posx = saveFigure.coordonnees.posx
        }
    }

    // Si la figure a touché le sol (bas d'ecran ou une autre figure)
    fun hasItGround(grille : Grille): Boolean {
        coordonnees.posy++
        if (isInObstacle(grille)){
            coordonnees.posy--
            return true
        }
        coordonnees.posy--
        return false
    }

    // Si la figure n'a pas d'obstacle a sa droite
    private fun hasRightObstacle(grille : Grille) : Boolean{
        coordonnees.posx++
        if (isInObstacle(grille)){
            coordonnees.posx--
            return true
        }
        coordonnees.posx--
        return false
    }

    // Si la figure n'a pas d'obstacle a sa gauche
    private fun hasLeftObstacle(grille : Grille) : Boolean{
        coordonnees.posx--
        if (isInObstacle(grille)){
            coordonnees.posx++
            return true
        }
        coordonnees.posx++
        return false
    }


    // Modifie les coordonée de la figure et ajoutant de 1 les coordonées y et en prenant en compte les
    // valeurs de l'acceléromètre
    fun updateCoordY(){
        coordonnees.posy += 1
    }

    fun updateCoordX(grille : Grille, sens : EnumsRL){
        if (sens == EnumsRL.LEFT ){
            if(!hasLeftObstacle(grille))
                coordonnees.posx--
        }
        if (sens == EnumsRL.RIGHT){
            if(!hasRightObstacle(grille))
                coordonnees.posx++
        }
    }

    fun drawWhenNextFigure(canvas: Canvas?, SIZE : Float){
        val paint = Paint()
        if (canvas != null) {
            for (i in 0 until hitBox){
                for (j in 0 until hitBox){
                    if( blocs[i][j] != null){
                        blocs[i][j]!!.color = color
                        paint.color = blocs[i][j]!!.color
                        canvas.drawRect(
                            j*SIZE,
                            i*SIZE,
                            j*SIZE + SIZE,
                            i*SIZE + SIZE,
                            paint);
                    }
                }
            }
        }
    }

    // Dessine la figure
    fun draw(canvas: Canvas?, SIZE : Float, CONST : Float){
        val paint = Paint()
        if (canvas != null) {
                for (i in 0 until hitBox){
                for (j in 0 until hitBox){
                    if( blocs[i][j] != null){
                        blocs[i][j]!!.color = color
                        paint.color = blocs[i][j]!!.color
                        canvas.drawRect(
                             j*SIZE + (coordonnees.posx*SIZE),
                            CONST+ i*SIZE + (coordonnees.posy*SIZE),
                            SIZE +(j*SIZE) + (coordonnees.posx*SIZE),
                            CONST +SIZE + (i*SIZE)  + (coordonnees.posy*SIZE),
                            paint);
                    }
                }
            }
        }
    }

}

