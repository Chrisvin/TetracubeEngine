package com.jem.tetracubeengine

import com.jem.tetracubeengine.listener.GameStateListener
import com.jem.tetracubeengine.pieces.Piece
import com.jem.tetracubeengine.util.PieceUtil
import java.util.*

/**
 * TetracubeEngine is used to "run" the entire game.
 * The player controls, board params, etc can all be accessed via the TetracubeEngine instance.
 */
class TetracubeEngine {

    /**
     * The board on which the Tetracube pieces are placed
     */
    var board: TetracubeBoard = TetracubeBoard()
        private set

    /**
     * The piece that is currently controlled by the player & is being dropped in the board
     */
    var currentPiece: Piece? = null
        private set

    /**
     * The piece that will be dropped next
     */
    var nextPiece: Piece = PieceUtil.getRandomPiece()
        private set

    /**
     * Listener to update client regarding state changes in the game
     */
    var gameStateListener: GameStateListener? = null

    /**
     * The last known X position of the current piece
     */
    private var previousX: Int = 0

    /**
     * The last known Y position of the current piece
     */
    private var previousY: Int = 0

    /**
     * The last known Z position of the current piece
     */
    private var previousZ: Int = 0

    /**
     * Timer to schedule the piece position update
     */
    private val tickTimer: Timer = Timer()

    /**
     * TimerTask that contains the logic of what happens each time the timer ticks.
     */
    private val gameTask: TimerTask = object : TimerTask() {
        override fun run() {
            currentPiece?.let {
                board.undo()
                previousY--
                val placeStatus = board.placePiece(it, previousX, previousY, previousZ)
                when (placeStatus) {
                    TetracubeBoard.PlaceStatus.SUCCESS -> {
                        // Was able to place piece successfully
                        // Check if piece can be moved further down next time.
                    }
                    TetracubeBoard.PlaceStatus.LAYER_FILLED -> {
                        // This shouldn't be occurring here.
                        // TODO: Add timber logs to check why this case is arising
                    }
                    TetracubeBoard.PlaceStatus.OUT_OF_BOUNDS -> {
                        if (previousY - (currentPiece?.height ?: 0) < 0) {
                            // Current piece hit the bottom of the grid
                            placeCurrentPieceAndCalculateScore()
                            prepareNewPiece()
                        } else {
                            // Shouldn't occur, but if it does, then it's probably one of the following
                            // 1. width/breadth for the grid was too small
                            // 2. previousX/previousZ is exceeding width/breadth limits
                            // TODO: Add timber log to check above cases
                        }
                    }
                    TetracubeBoard.PlaceStatus.OVERLAP_WITH_EXISTING -> {
                        // Current piece hit some other existing blocks on the grid
                        placeCurrentPieceAndCalculateScore()
                        prepareNewPiece()
                    }
                }
            } ?: run {
                // There's no reason this should ever occur
                // TODO: Add Timber logs to check why current piece is null
            }
        }
    }

    fun startNewGame(settings: TetracubeSettings = TetracubeSettings()) {
        board = TetracubeBoard(
            width = settings.boardWidth,
            height = settings.boardHeight,
            breadth = settings.boardBreadth
        )
        prepareNewPiece()
        tickTimer.scheduleAtFixedRate(gameTask, settings.initialTickDelay, settings.tickInterval)
    }

    private fun placeCurrentPieceAndCalculateScore() {
        board.undo()
        currentPiece?.let {
            val placeStatus =
                board.placePiece(it, previousX, previousY, previousZ + 1, checkLayerFilled = true)
            when (placeStatus) {
                TetracubeBoard.PlaceStatus.SUCCESS -> {
                    // Successfully placed, but no layers where cleared, no score
                }
                TetracubeBoard.PlaceStatus.LAYER_FILLED -> {
                    // Placing current piece causes 1 or more layers to be filled
                    val clearedLayers = board.clearLayers()
                    // TODO: Clear the rows and calculate the score
                }
                else -> {
                    // This shouldn't occur, placing the piece should definitely be a success
                    // TODO: Add timber log to check exactly what case this is & why it occurs
                }
            }
        } ?: run {
            // There's no reason this should ever occur
            // TODO: Add Timber logs to check why current piece is null
        }
    }

    private fun prepareNewPiece() {
        currentPiece = nextPiece
        nextPiece = PieceUtil.getRandomPiece()
        previousX = board.width / 2
        previousZ = board.breadth / 2
        previousY = board.height
        board.commit()
    }

}