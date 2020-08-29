package com.jem.tetracubeengine.pieces

import com.jem.tetracubeengine.model.Pair

class TPieceTest : PieceTest() {
    override val piece: Piece = TPiece()
    override val expectedBodyStates: ArrayList<ArrayList<Pair<Int, Int>>> = arrayListOf(
        arrayListOf(Pair(0, 1), Pair(1, 1), Pair(2, 1), Pair(1, 0)),
        arrayListOf(Pair(1, 2), Pair(1, 1), Pair(1, 0), Pair(0, 1)),
        arrayListOf(Pair(2, 0), Pair(1, 0), Pair(0, 0), Pair(1, 1)),
        arrayListOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(1, 1))
    )
}