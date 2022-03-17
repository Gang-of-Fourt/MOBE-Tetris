package com.example.tetris.models.figures

import android.graphics.Color
import com.example.tetris.models.Figure
import kotlin.random.Random

class RandomFigure {
    companion object {
        //Choisi une figure aléatoirement
        fun chooseFigure() : Figure {
            val randomId = Random.nextInt(0, 7)
            val color = Color.rgb(Random.nextInt(100, 200), Random.nextInt(100, 200), Random.nextInt(100, 200))
            when (randomId) {
                0 -> return Baton(color, Random.nextInt(0, 2))
                1 -> return Carre(color)
                2 -> return FormeZ1(color, Random.nextInt(0, 2))
                3 -> return FormeZ2(color, Random.nextInt(0, 2))
                4 -> return FormeL1(color, Random.nextInt(0, 4))
                5 -> return FormeL2(color, Random.nextInt(0, 4))
                else -> return FormeT(color, Random.nextInt(0, 4))
            }
        }
    }
}