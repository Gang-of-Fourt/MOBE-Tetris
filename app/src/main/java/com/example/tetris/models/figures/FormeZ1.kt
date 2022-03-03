package com.example.tetris.models.figures

import com.example.tetris.models.Bloc
import com.example.tetris.models.Coordonnees
import com.example.tetris.models.Figure

class FormeZ1(color : Int) : Figure("Z1", Coordonnees(4,0), color, 3, 2) {

    init{
        rotate0 = arrayOf (
            arrayOf(Bloc(color), null, null),
            arrayOf(Bloc(color), Bloc(color), null),
            arrayOf(null, Bloc(color), null)
        )
        rotate1 = arrayOf(
            arrayOf(null, null, null),
            arrayOf(null, Bloc(color), Bloc(color)),
            arrayOf(Bloc(color), Bloc(color), null)
        )
        blocs = rotate0
    }
}