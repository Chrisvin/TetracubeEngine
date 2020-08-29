package com.jem.tetracubeengine.pieces

import com.jem.tetracubeengine.model.Pair

class SPieceTest : PieceTest() {
    override val piece: Piece = SPiece()
    override val expectedBodyStates: ArrayList<ArrayList<Pair<Int, Int>>> = arrayListOf(
        arrayListOf(Pair(0, 0), Pair(1, 0), Pair(1, 1), Pair(2, 1)),
        arrayListOf(Pair(0, 2), Pair(0, 1), Pair(1, 1), Pair(1, 0)),
        arrayListOf(Pair(2, 1), Pair(1, 1), Pair(1, 0), Pair(0, 0)),
        arrayListOf(Pair(1, 0), Pair(1, 1), Pair(0, 1), Pair(0, 2))
    )
}