package com.jem.tetracubeengine.pieces

import com.jem.tetracubeengine.model.Pair

class OPieceTest : PieceTest() {
    override val piece: Piece = OPiece()
    override val expectedBodyStates: ArrayList<ArrayList<Pair<Int, Int>>> = arrayListOf(
        arrayListOf(Pair(0, 0), Pair(0, 1), Pair(1, 1), Pair(1, 0)),
        arrayListOf(Pair(0, 1), Pair(1, 1), Pair(1, 0), Pair(0, 0)),
        arrayListOf(Pair(1, 1), Pair(1, 0), Pair(0, 0), Pair(0, 1)),
        arrayListOf(Pair(1, 0), Pair(0, 0), Pair(0, 1), Pair(1, 1))
    )
}