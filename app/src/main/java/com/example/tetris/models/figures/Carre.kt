package com.example.tetris.models.figures

import com.example.tetris.models.Bloc
import com.example.tetris.models.Coordonnees
import com.example.tetris.models.Figure

class Carre(color : Int) : Figure("Carre", Coordonnees(4,0), color, 2, 1) {

    init {
        rotate0 = arrayOf(
            arrayOf(Bloc(color), Bloc(color)),
            arrayOf(Bloc(color), Bloc(color)),
        )
        blocs = rotate0
    }
}