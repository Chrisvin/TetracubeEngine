package com.jem.tetracubeengine

import com.jem.tetracubeengine.pieces.*
import junit.framework.Assert.assertEquals
import org.junit.Test

class TetracubeBoardTest {

    val pieces =
        arrayListOf<Piece>(JPiece(), IPiece(), LPiece(), OPiece(), SPiece(), TPiece(), ZPiece())

    @Test
    fun testDropHeight() {
        val board = TetracubeBoard()
        placePieceAndAssert(board, IPiece().apply { rotateClockwise() }, 5, 0, 0)
        for (piece in pieces) {
            assertEquals(
                4,
                board.dropHeight(piece, 4, 0)
            )
        }
        assertEquals(
            4,
            board.dropHeight(IPiece().apply { rotateClockwise() }, 5, 0)
        )
        placePieceAndAssert(
            board,
            IPiece().apply { rotateClockwise() },
            5,
            board.dropHeight(IPiece().apply { rotateClockwise() }, 5, 0),
            0
        )
        for (piece in pieces) {
            assertEquals(
                8,
                board.dropHeight(piece, 4, 0)
            )
        }
        assertEquals(
            8,
            board.dropHeight(IPiece().apply { rotateClockwise() }, 5, 0)
        )
        placePieceAndAssert(board, IPiece(), 2, 8, 0)
        for (piece in pieces) {
            println(piece.pieceName)
            assertEquals(
                0,
                board.dropHeight(piece, 1, 0, 4)
            )
            assertEquals(
                0,
                board.dropHeight(piece.apply { rotateCounterClockwise() }, 1, 0, 4)
            )
        }
    }

    private fun placePieceAndAssert(board: TetracubeBoard, piece: Piece, x: Int, y: Int, z: Int) {
        assert(board.placePiece(piece, x, y, z) == TetracubeBoard.PlaceStatus.SUCCESS)
    }
}