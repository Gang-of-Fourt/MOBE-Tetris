package com.example.tetris.models.figures

import com.example.tetris.models.Bloc
import com.example.tetris.models.Coordonnees
import com.example.tetris.models.Figure

class FormeT(color : Int) : Figure("T", Coordonnees(0,0), color, 3, 4) {
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
            arrayOf(Bloc(color), Bloc(color), Bloc(color)),
            arrayOf(null, Bloc(color), null),
            arrayOf(null, null, null)
        )

        rotate3 = arrayOf(
            arrayOf(null, null, Bloc(color)),
            arrayOf(null, Bloc(color), Bloc(color)),
            arrayOf(null, null, Bloc(color))
        )

        blocs = rotate0
    }

}