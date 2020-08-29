package com.jem.tetracubeengine.handler

import com.jem.tetracubeengine.model.Pair
import com.jem.tetracubeengine.pieces.Piece
import com.jem.tetracubeengine.util.CompareUtil

object PieceTestHandler {

    fun testRotation(piece: Piece, expectedBodyStates: ArrayList<ArrayList<Pair<Int, Int>>>) {
        println(piece.pieceName)
        assert(CompareUtil.compareBodyState(piece.body, expectedBodyStates[0]))
        piece.rotateClockwise()
        assert(CompareUtil.compareBodyState(piece.body, expectedBodyStates[1]))
        piece.rotateClockwise()
        assert(CompareUtil.compareBodyState(piece.body, expectedBodyStates[2]))
        piece.rotateClockwise()
        assert(CompareUtil.compareBodyState(piece.body, expectedBodyStates[3]))
        piece.rotateClockwise()
        assert(CompareUtil.compareBodyState(piece.body, expectedBodyStates[0]))

        piece.rotateClockwise()
        assert(CompareUtil.compareBodyState(piece.body, expectedBodyStates[1]))
        piece.rotateCounterClockwise()
        assert(CompareUtil.compareBodyState(piece.body, expectedBodyStates[0]))
        piece.rotateCounterClockwise()
        assert(CompareUtil.compareBodyState(piece.body, expectedBodyStates[3]))
        piece.rotateCounterClockwise()
        assert(CompareUtil.compareBodyState(piece.body, expectedBodyStates[2]))
        piece.rotateCounterClockwise()
        assert(CompareUtil.compareBodyState(piece.body, expectedBodyStates[1]))
        piece.rotateCounterClockwise()
        assert(CompareUtil.compareBodyState(piece.body, expectedBodyStates[0]))
        println()
    }
}