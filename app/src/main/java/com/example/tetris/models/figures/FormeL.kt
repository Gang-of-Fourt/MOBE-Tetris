package com.example.tetris.models.figures

import com.example.tetris.models.Bloc
import com.example.tetris.models.Coordonnees
import com.example.tetris.models.EnumSens
import com.example.tetris.models.Figure

class FormeL(color : Int) : Figure("L", Coordonnees(4,0), color, 3, 4) {

    init {
        rotate0 = arrayOf (
            arrayOf(null, Bloc(color), null),
            arrayOf(null, Bloc(color), null),
            arrayOf(null, Bloc(color), Bloc(color))
        )

        rotate1 = arrayOf(
            arrayOf(null, null, null),
            arrayOf(Bloc(color), Bloc(color), Bloc(color)),
            arrayOf(Bloc(color), null, null)
        )

        rotate2 = arrayOf(
            arrayOf(Bloc(color), Bloc(color), null),
            arrayOf(null, Bloc(color), null),
            arrayOf(null, Bloc(color), null)
        )

        rotate3 = arrayOf(
            arrayOf(null, null, Bloc(color)),
            arrayOf(Bloc(color), Bloc(color), Bloc(color)),
            arrayOf(null, null, null)
        )

        blocs = rotate0
    }



}