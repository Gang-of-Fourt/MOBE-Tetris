package com.example.tetris.models.figures

import android.graphics.Color
import com.example.tetris.models.Figure
import kotlin.random.Random

class RandomFigure {
    companion object {
        //Choisi une figure aléatoirement
        fun chooseFigure() : Figure {
            val randomId = Random.nextInt(0, 6)
            val color = Color.argb(Random.nextInt(100, 255), Random.nextInt(100, 255), Random.nextInt(100, 255), 1)
            when (randomId) {
                0 -> return Baton(color, Random.nextInt(0, 2))
                1 -> return Carre(color)
                2 -> return FormeZ1(color, Random.nextInt(0, 2))
                3 -> return FormeZ2(color, Random.nextInt(0, 2))
                4 -> return FormeL(color, Random.nextInt(0, 4))
                else -> return FormeT(color, Random.nextInt(0, 4))
            }
        }
    }
}