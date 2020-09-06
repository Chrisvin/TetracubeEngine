package com.jem.tetracubeengine

import com.jem.tetracubeengine.pieces.*
import junit.framework.Assert
import org.junit.Test

class TetracubeBoardTest {

    val pieces =
        arrayListOf<Piece>(JPiece(), IPiece(), LPiece(), OPiece(), SPiece(), TPiece(), ZPiece())

    @Test
    fun testDropHeight() {
        val board = TetracubeBoard()
        board.placePiece(IPiece().apply { rotateClockwise() }, 5, 0, 0)
        for (piece in pieces) {
            println(piece.pieceName)
            Assert.assertEquals(
                4,
                board.dropHeight(piece, 4, 0)
            )
        }
        Assert.assertEquals(
            4,
            board.dropHeight(IPiece().apply { rotateClockwise() }, 5, 0)
        )
        board.placePiece(
            IPiece().apply { rotateClockwise() },
            5,
            board.dropHeight(IPiece().apply { rotateClockwise() }, 5, 0),
            0
        )
        for (piece in pieces) {
            println(piece.pieceName)
            Assert.assertEquals(
                8,
                board.dropHeight(piece, 4, 0)
            )
        }
        Assert.assertEquals(
            8,
            board.dropHeight(IPiece().apply { rotateClockwise() }, 5, 0)
        )
    }
}