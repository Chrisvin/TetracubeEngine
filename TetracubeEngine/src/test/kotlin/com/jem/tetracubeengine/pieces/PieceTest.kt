package com.jem.tetracubeengine.pieces

import com.jem.tetracubeengine.handler.PieceTestHandler
import com.jem.tetracubeengine.model.Pair

abstract class PieceTest {

    abstract val piece: Piece
    abstract val expectedBodyStates: ArrayList<ArrayList<Pair<Int, Int>>>

    @org.junit.Test
    open fun testBodyStates() {
        PieceTestHandler.testRotation(piece, expectedBodyStates)
    }

}