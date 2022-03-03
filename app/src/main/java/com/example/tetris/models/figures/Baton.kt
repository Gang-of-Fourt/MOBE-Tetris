package com.example.tetris.models.figures
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.tetris.models.Bloc
import com.example.tetris.models.Coordonnees
import com.example.tetris.models.Figure



class Baton(color : Int) : Figure("Baton", Coordonnees(4,0), color,4, 2 ) {

    init {
        rotate0 = arrayOf(
            arrayOf(null, null, null, null),
            arrayOf(null, null, null, null),
            arrayOf(null, null, null, null),
            arrayOf(Bloc(color), Bloc(color), Bloc(color), Bloc(color)),

        )

        rotate1 = arrayOf(
            arrayOf(null, Bloc(color), null, null),
            arrayOf(null, Bloc(color), null, null),
            arrayOf(null, Bloc(color), null, null),
            arrayOf(null, Bloc(color), null, null),
        )

        blocs = rotate1


    }

 }


