package com.jem.tetracubeengine.pieces

import com.jem.tetracubeengine.model.Pair

class JPiece : Piece() {
    override val pieceName: String = "J-Piece"

    override fun getInitialBody(): ArrayList<Pair<Int, Int>> = arrayListOf(
        Pair(0, 0),
        Pair(1, 0),
        Pair(1, 1),
        Pair(1, 2)
    )
}