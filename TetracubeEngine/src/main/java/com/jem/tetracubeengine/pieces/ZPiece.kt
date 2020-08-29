package com.jem.tetracubeengine.pieces

import com.jem.tetracubeengine.model.Pair

class ZPiece : Piece() {
    override val pieceName: String = "Z-Piece"

    override fun getInitialBody(): ArrayList<Pair<Int, Int>> = arrayListOf(
        Pair(0, 1),
        Pair(1, 1),
        Pair(1, 0),
        Pair(2, 0)
    )
}