package com.example.tetris.models

class Coordonnees(
    var posx: Int,
    var posy: Int
) {
    override fun equals(other: Any?) =
        (other is Coordonnees) && posx == other.posx && posy == other.posy

    override fun hashCode(): Int {
        var result = posx
        result = 31 * result + posy
        return result
    }
}
