package com.example.tetris.models

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build

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

    var canSave = true

    var colorBordure = Color.rgb( 255, 255, 255)


    // Fait une copie de la figure et la retourne
    fun doCopy() : Figure {
        val saveFigure = Figure(nom, coordonnees, color, hitBox, nbRotate, currentRotate)
        saveFigure.blocs = blocs.copyOf()
        saveFigure.rotate0 = rotate0.copyOf()
        if (nbRotate > 1) {
            saveFigure.rotate1 = rotate1.copyOf()
            if (nbRotate > 2) {
                saveFigure.rotate2 = rotate2.copyOf()
                saveFigure.rotate3 = rotate3.copyOf()
            }
        }
        return saveFigure
    }

    // Vérifie sir la figure courante est dans un obstacle (autre figure ou en dehors de la grille)
    fun isInObstacle(grille : Grille) : Boolean{
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


    // Fait desendre la figure
    fun updateCoordY(){
        coordonnees.posy += 1
    }

    // Décale la figure à droite ou à gauche
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

    fun changeColorLight(lightSensor : Float, lightConstant : Int){
        val alpha = (255-((255F / lightConstant.toFloat()) * lightSensor).toInt()).coerceAtLeast(0)
        if (canSave) {
            colorBordure = Color.argb(alpha, 0, 0, 255)
        }
        else {
            colorBordure = Color.argb(alpha, 255, 0, 0)
        }
    }

    fun resetCoord(){
        coordonnees.posy = 0
        coordonnees.posx = 5
    }



    private fun drawBordure(canvas: Canvas?, SIZE : Float, CONSTX : Float, CONSTY : Float){
        val paint = Paint()
        paint.color = colorBordure
        if (canvas != null) {
            for (i in 0 until hitBox){
                for (j in 0 until hitBox){
                    if( blocs[i][j] != null){
                        canvas.drawRect(
                            CONSTY + j*SIZE + (coordonnees.posx*SIZE) -20F,
                            CONSTX+ i*SIZE + (coordonnees.posy*SIZE) -20F,
                            CONSTY + SIZE +(j*SIZE) + (coordonnees.posx*SIZE) + 20F,
                            CONSTX +SIZE + (i*SIZE)  + (coordonnees.posy*SIZE) + 20F,
                            paint)

                    }
                }
            }
        }
    }

    // Dessine la figure
    fun draw(canvas: Canvas?, SIZE : Float, CONSTX : Float, CONSTY : Float){
        val paint = Paint()
        drawBordure(canvas, SIZE, CONSTX, CONSTY)
        if (canvas != null) {
                for (i in 0 until hitBox){
                for (j in 0 until hitBox){
                    if( blocs[i][j] != null){
//                        blocs[i][j]!!.color = color
                        paint.color = color
                        canvas.drawRect(
                            CONSTY + j*SIZE + (coordonnees.posx*SIZE),
                            CONSTX+ i*SIZE + (coordonnees.posy*SIZE),
                            CONSTY + SIZE +(j*SIZE) + (coordonnees.posx*SIZE),
                            CONSTX +SIZE + (i*SIZE)  + (coordonnees.posy*SIZE),
                            paint)

                        paint.color = Color.argb(30,255,255, 255)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            canvas.drawRoundRect(
                                CONSTY + j*SIZE + SIZE*1/8 + (coordonnees.posx*SIZE),
                                CONSTX + i*SIZE + SIZE*1/8 + (coordonnees.posy*SIZE),
                                CONSTY + SIZE +(j*SIZE) - SIZE*1/8 + (coordonnees.posx*SIZE),
                                CONSTX + SIZE + (i*SIZE) - SIZE*1/8+ (coordonnees.posy*SIZE),
                                10F,
                                10F,
                                paint)
                        };
                    }
                }
            }
        }

    }

}

