package com.example.tetris.models.figures

import com.example.tetris.models.Bloc
import com.example.tetris.models.Coordonnees
import com.example.tetris.models.Figure

class FormeZ2(color : Int, currentRotate : Int) : Figure("Z2", Coordonnees(4,0), color, 3, 2, currentRotate) {

    init{
        rotate0 = arrayOf (
            arrayOf(null, Bloc(color), null),
            arrayOf(Bloc(color), Bloc(color), null),
            arrayOf(Bloc(color), null, null)
        )
        rotate1 = arrayOf(
            arrayOf(null, null, null),
            arrayOf(Bloc(color), Bloc(color), null),
            arrayOf(null, Bloc(color), Bloc(color))
        )
        blocs = initFigure()
    }

    private fun initFigure() : Array<Array<Bloc?>>{
        when(currentRotate){
            0 -> return rotate0
            else -> return rotate1
        }
    }
}