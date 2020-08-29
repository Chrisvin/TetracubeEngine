package com.jem.tetracubeengine.pieces

import com.jem.tetracubeengine.model.Pair

class LPieceTest : PieceTest() {

    override val piece: Piece = LPiece()

    override val expectedBodyStates = arrayListOf(
        arrayListOf(Pair(0, 2), Pair(0, 1), Pair(0, 0), Pair(1, 0)),
        arrayListOf(Pair(2, 1), Pair(1, 1), Pair(0, 1), Pair(0, 0)),
        arrayListOf(Pair(1, 0), Pair(1, 1), Pair(1, 2), Pair(0, 2)),
        arrayListOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(2, 1))
    )
}