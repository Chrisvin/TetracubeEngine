package com.jem.tetracubeengine.pieces

import com.jem.tetracubeengine.model.Pair

class IPiece : Piece() {
    override val pieceName: String = "I-Piece"

    override fun getInitialBody(): ArrayList<Pair<Int, Int>> = arrayListOf(
        Pair(0, 0),
        Pair(1, 0),
        Pair(2, 0),
        Pair(3, 0)
    )
}