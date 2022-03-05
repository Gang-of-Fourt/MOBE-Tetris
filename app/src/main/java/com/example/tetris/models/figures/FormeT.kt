package com.example.tetris.models.figures

import com.example.tetris.models.Bloc
import com.example.tetris.models.Coordonnees
import com.example.tetris.models.Figure

class FormeT(color : Int, currentRotate : Int) : Figure("T", Coordonnees(4,0), color, 3, 4, currentRotate) {
    init {
        rotate0 = arrayOf (
            arrayOf(null, null, null),
            arrayOf(null, Bloc(color), null),
            arrayOf(Bloc(color), Bloc(color), Bloc(color))
        )

        rotate1 = arrayOf(
            arrayOf(Bloc(color), null, null),
            arrayOf(Bloc(color), Bloc(color), null),
            arrayOf(Bloc(color), null, null)
        )

        rotate2 = arrayOf(
            arrayOf(null, null, null),
            arrayOf(Bloc(color), Bloc(color), Bloc(color)),
            arrayOf(null, Bloc(color), null),

        )

        rotate3 = arrayOf(
            arrayOf( null, Bloc(color), null),
            arrayOf( Bloc(color), Bloc(color), null),
            arrayOf( null,Bloc(color), null)
        )

        blocs = initFigure()
    }

    private fun initFigure() : Array<Array<Bloc?>>{
        when(currentRotate){
            0 -> return rotate0
            1 -> return rotate1
            2 -> return rotate2
            else -> return rotate3
        }
    }

}