package com.jem.tetracubeengine.pieces

import com.jem.tetracubeengine.model.Pair

class TPiece : Piece() {
    override val pieceName: String = "T-Piece"

    override fun getInitialBody(): ArrayList<Pair<Int, Int>> = arrayListOf(
        Pair(0, 1),
        Pair(1, 1),
        Pair(2, 1),
        Pair(1, 0)
    )
}