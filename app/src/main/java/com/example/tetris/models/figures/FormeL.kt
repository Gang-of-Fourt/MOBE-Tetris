package com.example.tetris.models.figures

import com.example.tetris.models.Bloc
import com.example.tetris.models.Coordonnees
import com.example.tetris.models.Figure

class FormeL(color : Int) : Figure("baton", Coordonnees(0,0), color) {
    init {
        blocs = arrayOf(
            arrayOf(null, Bloc(color), null),
            arrayOf(null, Bloc(color), null),
            arrayOf(null, Bloc(color), Bloc(color))
        )
    }

}