package com.example.tetris.models

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

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
        val liste = mutableListOf<Int>()
        for (i in 0 until height){
            blocInLine = 0
            for (j in 0 until width){
                if(cases[i][j] != null){
                    blocInLine++
                }
            }
            if (blocInLine == width){
                liste.add(i)
            }
        }
        return liste
    }

    // Si le joueur a perdu
    fun isGameOver() : Boolean{
        for (i in 0 until width) {
           if (cases[0][i] != null){
               return true
           }
        }
        return false
    }

    // Dessine le contenue de la grille
    fun draw(canvas : Canvas) {
        for (i in 0 until height){
            for (j in 0 until width){
                if(cases[i][j] != null){
                    val paint = Paint()
                    paint.color = cases[i][j]!!.color
                    canvas.drawRect(0F + (j*100), 0F +(i*100), 100F +(j*100), 100F + (i*100), paint);
//                    canvas.drawText("test",0, 1, 50F, 50F, paint)

                }
            }
        }
    }

}