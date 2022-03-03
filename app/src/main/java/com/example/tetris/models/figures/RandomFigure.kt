package com.example.tetris.models.figures

import android.graphics.Color
import com.example.tetris.models.Figure
import kotlin.random.Random

class RandomFigure {
    companion object {
        fun chooseFigure() : Figure {
            val randomId = Random.nextInt(0, 6)
            when (randomId) {
                0 -> return Baton(Color.BLUE)
                1 -> return Carre(Color.RED)
                2 -> return FormeZ1(Color.GREEN)
                3 -> return FormeZ2(Color.YELLOW)
                4 -> return FormeL(Color.DKGRAY)
                else -> return FormeT(Color.CYAN)
            }
        }
    }
}