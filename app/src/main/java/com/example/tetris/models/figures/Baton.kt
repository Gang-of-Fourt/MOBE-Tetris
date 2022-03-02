package com.example.tetris.models.figures
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.tetris.models.Bloc
import com.example.tetris.models.Coordonnees
import com.example.tetris.models.Figure



class Baton(color : Int) : Figure("baton", Coordonnees(0,0), color) {

    init {
        blocs = arrayOf(
            arrayOf(null, null, null),
            arrayOf(null, null, null),
            arrayOf(Bloc(color), Bloc(color), Bloc(color))

        )
    }

 }


