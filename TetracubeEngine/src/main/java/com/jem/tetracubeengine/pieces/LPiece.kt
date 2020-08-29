package com.jem.tetracubeengine.pieces

import com.jem.tetracubeengine.model.Pair

class LPiece : Piece() {
    override val pieceName: String = "L-Piece"

    override fun getInitialBody(): ArrayList<Pair<Int, Int>> = arrayListOf(
        Pair(0, 0),
        Pair(1, 0),
        Pair(1, 1),
        Pair(1, 2)
    )
}