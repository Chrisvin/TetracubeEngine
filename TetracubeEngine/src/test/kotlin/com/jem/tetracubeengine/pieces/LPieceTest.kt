package com.jem.tetracubeengine.pieces

import com.jem.tetracubeengine.model.Pair

class LPieceTest {

    val expectedBodyStates = arrayListOf(
        arrayListOf(Pair(0, 0), Pair(1, 0), Pair(1, 1), Pair(1, 2)),
        arrayListOf(Pair(0, 1), Pair(0, 0), Pair(1, 0), Pair(2, 0)),
        arrayListOf(Pair(1, 2), Pair(0, 2), Pair(0, 1), Pair(0, 0)),
        arrayListOf(Pair(2, 0), Pair(2, 1), Pair(1, 1), Pair(0, 1))
    )

    @org.junit.Test
    fun testBodyStates() {
        val piece = LPiece()
        assert(compareBodyState(piece.body, expectedBodyStates[0]))
        piece.rotateClockwise()
        assert(compareBodyState(piece.body, expectedBodyStates[1]))
        piece.rotateClockwise()
        assert(compareBodyState(piece.body, expectedBodyStates[2]))
        piece.rotateClockwise()
        assert(compareBodyState(piece.body, expectedBodyStates[3]))
        piece.rotateClockwise()
        assert(compareBodyState(piece.body, expectedBodyStates[0]))

        piece.rotateClockwise()
        assert(compareBodyState(piece.body, expectedBodyStates[1]))
        piece.rotateCounterClockwise()
        assert(compareBodyState(piece.body, expectedBodyStates[0]))
        piece.rotateCounterClockwise()
        assert(compareBodyState(piece.body, expectedBodyStates[3]))
        piece.rotateCounterClockwise()
        assert(compareBodyState(piece.body, expectedBodyStates[2]))
        piece.rotateCounterClockwise()
        assert(compareBodyState(piece.body, expectedBodyStates[1]))
        piece.rotateCounterClockwise()
        assert(compareBodyState(piece.body, expectedBodyStates[0]))
    }

    private fun compareBodyState(
        actualBody: ArrayList<Pair<Int, Int>>,
        expectedBody: ArrayList<Pair<Int, Int>>
    ): Boolean {
        println(actualBody.joinToString())
        if (actualBody.size != expectedBody.size) {
            return false
        }
        for (i in 0 until expectedBody.size) {
            if (!actualBody[i].equals(expectedBody[i])) {
                return false
            }
        }
        return true
    }
}