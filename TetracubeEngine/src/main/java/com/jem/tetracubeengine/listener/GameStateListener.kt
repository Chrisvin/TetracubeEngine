package com.jem.tetracubeengine.listener

import com.jem.tetracubeengine.TetracubeSettings
import com.jem.tetracubeengine.pieces.Piece

interface GameStateListener {
    /**
     * Called when new game is started with [settings]
     */
    fun onGameStarted(settings: TetracubeSettings)

    /**
     * Called when blocks in the [grid] have been changed and UI needs to be updated
     */
    fun onGridUpdated(grid: Array<Array<BooleanArray>>, height: Int, width: Int, breadth: Int)

    /**
     * Called when pieces are changed
     */
    fun onPieceChanged(previousPiece: Piece, currentPiece: Piece, nextPiece: Piece)

    /**
     * Called when game ends
     */
    fun onGameEnded()
    //TODO: Pass final score when game ends
}