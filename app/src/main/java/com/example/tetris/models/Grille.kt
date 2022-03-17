package com.example.tetris.models

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi

class Grille(
    var height : Int,
    var width : Int,

) {
    var cases: MutableList<MutableList<Bloc?>> = MutableList(height) {MutableList(width+2) {null} }

    // Met la figure donnée en paramètre dans la grille
    fun update(figure : Figure){
        for (i in 0 until figure.hitBox) {
            for (j in 0 until figure.hitBox) {
                if (figure.blocs[i][j] != null) {
                    cases[figure.coordonnees.posy + i][figure.coordonnees.posx + j] =
                        figure.blocs[i][j]
                }
            }
        }
    }


    // Permet de supprimer un bloc de la grille, avec ses coordonnees en paramètres
    // en faisant ''tomber'' les bocs au dessus de lui
    private fun dropBlocs(coordonnees: Coordonnees){
        var posy = coordonnees.posy
        cases[posy][coordonnees.posx] = null
        while( posy > 1 ) {
            cases[posy][coordonnees.posx] =
                cases[posy - 1][coordonnees.posx]
            posy--
        }
        cases[0][coordonnees.posx] = null

    }

    // Supprime une ligne de la grille en faisait tomber celle au dessus d'elle
    fun deletLines(lines : List<Int>){
        lines.forEach {
            println("Lines = " + it)
            for (j in 0 until width) {
                println("COORDONEE" + j)
                dropBlocs(Coordonnees(j, it))
            }
        }
    }

    // Vérifie si une  ou plusieurs lignes sont remplies et retourne leurs coordonée dans une liste
    fun isLineFull() : List<Int>{
        var blocInLine : Int
        val listeTab = mutableListOf<Int>()
        for (i in 0 until height){
            blocInLine = 0
            for (j in 0 until width){
                if(cases[i][j] != null){
                    blocInLine++
                }
            }
            if (blocInLine == width){
                listeTab.add(i)
            }
        }
        return listeTab
    }

    // Si le joueur a perdu
    fun isGameOver() : Boolean{
//        if(figureCurrent)
        for (i in 0 until width) {
           if (cases[0][i] != null){
               return true
           }
        }
        return false
    }

    //Dessine une case du fond de la grille
    private fun drawBackground(canvas : Canvas, SIZE : Float, CONSTX : Float, CONSTY : Float, i : Int, j : Int){
        val paint = Paint()
        paint.color = Color.rgb(30,30,30)
        canvas.drawRect(CONSTY + j*SIZE, CONSTX + i*SIZE, CONSTY + SIZE +(j*SIZE), CONSTX + SIZE + (i*SIZE), paint);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            paint.color = Color.BLACK
            canvas.drawRoundRect(CONSTY + j*SIZE, CONSTX + i*SIZE, CONSTY + SIZE +(j*SIZE), CONSTX + SIZE + (i*SIZE),20F, 20F, paint)
            paint.color = Color.rgb(20,20,20)
            canvas.drawRoundRect(CONSTY + j*SIZE + SIZE*1/4, CONSTX + i*SIZE + SIZE*1/4, CONSTY + SIZE +(j*SIZE) - SIZE*1/4, CONSTX + SIZE + (i*SIZE) - SIZE*1/4,10F, 10F, paint);
        };

    }

    // Dessine le contenue de la grille
    fun draw(canvas : Canvas, SIZE : Float, CONSTX : Float, CONSTY : Float) {
        for (i in -1 until height){
            for (j in -1 until width + 1){
                val paint = Paint()
                // Dessine les bordures
                if (i < 0 || j >= width || j < 0 ){
                    paint.color = Color.BLACK
                    canvas.drawRect(CONSTY + j*SIZE, CONSTX + i*SIZE, CONSTY + SIZE +(j*SIZE), CONSTX + SIZE + (i*SIZE), paint);
                } else {
                    // Dessine le contenue de la grille
                     if(cases[i][j] != null){
                        paint.color = cases[i][j]!!.color
                        canvas.drawRect( CONSTY + j*SIZE, CONSTX + i*SIZE, CONSTY + SIZE+(j*SIZE), CONSTX + SIZE + (i*SIZE), paint);
                         paint.color = Color.argb(30,255,255, 255)
                         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                             canvas.drawRoundRect(
                                 CONSTY + j*SIZE + SIZE*1/8,
                                 CONSTX + i*SIZE + SIZE*1/8,
                                 CONSTY + SIZE+(j*SIZE) - SIZE*1/8,
                                 CONSTX + SIZE + (i*SIZE) - SIZE*1/8,
                                 10F,
                                 10F,
                                 paint)
                         };
                     }
                     // Dessine le fond de la grille
                     else {
                         drawBackground(canvas, SIZE, CONSTX, CONSTY, i, j)
                     }
                }
            }
        }
    }
}

//if (cases[i][j] != null) {
//    paint.color = cases[i][j]!!.color
//} else {
//    paint.color = Color.LTGRAY
//}
//canvas.drawRect(
//CONSTY + j * SIZE,
//CONSTX + i * SIZE,
//CONSTY + SIZE + (j * SIZE),
//CONSTX + SIZE + (i * SIZE),
//paint
//);