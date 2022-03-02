package com.example.tetris.models.figures

import com.example.tetris.models.Bloc
import com.example.tetris.models.Coordonnees
import com.example.tetris.models.Figure

class Carre(color : Int) : Figure("baton", Coordonnees(4,0), color) {

    init {
        blocs = arrayOf(
            arrayOf(null, null, null),
            arrayOf(Bloc(color), Bloc(color), null),
            arrayOf(Bloc(color), Bloc(color), null),

        )
    }
}