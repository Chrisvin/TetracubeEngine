package com.jem.tetracubeengine.pieces

import com.jem.tetracubeengine.model.Pair

class IPieceTest : PieceTest() {

    override val piece: Piece = IPiece()

    override val expectedBodyStates = arrayListOf(
        arrayListOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(3, 0)),
        arrayListOf(Pair(0, 3), Pair(0, 2), Pair(0, 1), Pair(0, 0)),
        arrayListOf(Pair(3, 0), Pair(2, 0), Pair(1, 0), Pair(0, 0)),
        arrayListOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(0, 3))
    )
}