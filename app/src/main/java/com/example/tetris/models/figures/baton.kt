package com.example.tetris.models.figures
import com.example.tetris.models.Bloc
import com.example.tetris.models.Coordonnees
import com.example.tetris.models.Figure



class Baton : Figure("coucou", Coordonnees("1","2")) {

    init { // a refaire
        var block=arrayOf(arrayOf(Bloc("test", Coordonnees("1", "2"))))
        this.blocs = block
 }


}