package com.jem.tetracubeengine.pieces

import com.jem.tetracubeengine.model.Pair

class OPiece : Piece() {
    override val pieceName: String = "O-Piece"

    override fun getInitialBody(): ArrayList<Pair<Int, Int>> = arrayListOf(
        Pair(0, 0),
        Pair(0, 1),
        Pair(1, 1),
        Pair(1, 0)
    )
}