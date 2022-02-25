package com.example.tetris.models

open class Figure(
    var nom: String,
    var coordonnees: Coordonnees
) : IRotatable {

    lateinit var blocs: Array<Array<Bloc>>
    override fun rotate(sens: EnumSens) { // fonction rotate tableau matrice carre
    }

}

