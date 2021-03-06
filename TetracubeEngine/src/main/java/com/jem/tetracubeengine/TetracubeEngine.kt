package com.jem.tetracubeengine

import com.jem.tetracubeengine.listener.GameStateListener
import com.jem.tetracubeengine.pieces.OPiece
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
    var currentPiece: Piece = OPiece()
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

    var settings: TetracubeSettings = TetracubeSettings()
        private set

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
    private var tickTimer: Timer? = null

    /**
     * TimerTask that contains the logic of what happens each time the timer ticks.
     */
    private val gameTask: Runnable = Runnable {
        board.undo()
        previousY--
        val placeStatus = board.placePiece(currentPiece, previousX, previousY, previousZ)
        when (placeStatus) {
            TetracubeBoard.PlaceStatus.SUCCESS -> {
                // Was able to place piece successfully
                // Check if piece can be moved further down next time.
                gameStateListener?.onGridUpdated(
                    board.grid,
                    board.height,
                    board.width,
                    board.breadth
                )
            }
            TetracubeBoard.PlaceStatus.LAYER_FILLED -> {
                // This shouldn't be occurring here.
                // TODO: Add timber logs to check why this case is arising
            }
            TetracubeBoard.PlaceStatus.OUT_OF_BOUNDS -> {
                if (previousY - currentPiece.height < 0) {
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
                if (previousY == board.height - currentPiece.height) {
                    stopGame()
                } else {
                    // Current piece hit some other existing blocks on the grid
                    placeCurrentPieceAndCalculateScore()
                    prepareNewPiece()
                }
            }
        }
    }

    @JvmOverloads
    fun startNewGame(settings: TetracubeSettings = TetracubeSettings()) {
        tickTimer?.cancel()
        tickTimer?.purge()
        this.settings = settings
        board = TetracubeBoard(
            width = this.settings.boardWidth,
            height = this.settings.boardHeight,
            breadth = this.settings.boardBreadth
        )
        prepareNewPiece()
        tickTimer = Timer()
        tickTimer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                gameTask.run()
            }
        }, this.settings.initialTickDelay, this.settings.tickInterval)
        gameStateListener?.onGameStarted(this.settings)
    }

    fun resumeGame() {
        tickTimer?.cancel()
        tickTimer?.purge()
        tickTimer = Timer()
        tickTimer?.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    gameTask.run()
                }
            },
            // Resume with initial delay as tickInterval (because it makes more sense from UX perspective IMO)
            this.settings.tickInterval,
            this.settings.tickInterval
        )
        gameStateListener?.onGameResumed()
    }

    fun pauseGame() {
        tickTimer?.cancel()
        tickTimer?.purge()
        tickTimer = null
        gameStateListener?.onGamePaused()
    }

    fun stopGame() {
        tickTimer?.cancel()
        tickTimer?.purge()
        tickTimer = null
        gameStateListener?.onGameEnded()
    }

    private fun placeCurrentPieceAndCalculateScore() {
        board.undo()
        val placeStatus = board.placePiece(
            currentPiece,
            previousX, previousY + 1, previousZ,
            checkLayerFilled = true
        )
        when (placeStatus) {
            TetracubeBoard.PlaceStatus.SUCCESS -> {
                // Successfully placed, but no layers where cleared, no score
            }
            TetracubeBoard.PlaceStatus.LAYER_FILLED -> {
                // Placing current piece causes 1 or more layers to be filled
                val clearedLayers = board.clearLayers()
                // TODO: Clear the rows and calculate the score

                // TODO: Consider whether maybe this should just be a callback to the app,
                //  and the app decides when the layers will be cleared?
                //  So as to allow time for clear animations.
            }
            else -> {
                // This shouldn't occur, placing the piece should definitely be a success
                // TODO: Add timber log to check exactly what case this is & why it occurs
            }
        }
        gameStateListener?.onGridUpdated(board.grid, board.height, board.width, board.breadth)
    }

    fun hardDrop() {
        board.undo()
        previousY = board.dropHeight(currentPiece, previousX, previousZ, previousY)
        // Increment because the value is decremented in gameTask
        previousY++
        gameTask.run()
        // TODO: Add flag for hard drop and multiply layer fill score accordingly
    }

    fun dropOneLayer() {
        gameTask.run()
        // TODO: Add flag for soft drop and multiply layer fill score accordingly
    }

    fun moveLeft() {
        if (previousX == 0) {
            return
        }
        val oldX = previousX
        previousX--
        board.undo()
        val placeStatus = board.placePiece(currentPiece, previousX, previousY, previousZ)
        when (placeStatus) {
            TetracubeBoard.PlaceStatus.OVERLAP_WITH_EXISTING, TetracubeBoard.PlaceStatus.OUT_OF_BOUNDS -> {
                // Not a valid move, revert to previous state
                // TODO: Check if it's possible to check for this case, instead of doing & then reverting
                previousX = oldX
                board.undo()
                board.placePiece(currentPiece, previousX, previousY, previousZ)
            }
            else -> {
                // It's valid, peace out.
            }
        }
        gameStateListener?.onGridUpdated(board.grid, board.height, board.width, board.breadth)
    }

    fun moveRight() {
        if (previousX + currentPiece.width > board.width) {
            return
        }
        val oldX = previousX
        previousX++
        board.undo()
        val placeStatus = board.placePiece(currentPiece, previousX, previousY, previousZ)
        when (placeStatus) {
            TetracubeBoard.PlaceStatus.OVERLAP_WITH_EXISTING, TetracubeBoard.PlaceStatus.OUT_OF_BOUNDS -> {
                // Not a valid move, revert to previous state
                // TODO: Check if it's possible to check for this case, instead of doing & then reverting
                previousX = oldX
                board.undo()
                board.placePiece(currentPiece, previousX, previousY, previousZ)
            }
            else -> {
                // It's valid, peace out.
            }
        }
        gameStateListener?.onGridUpdated(board.grid, board.height, board.width, board.breadth)
    }

    fun rotateClockwise() {
        currentPiece.rotateClockwise()
        board.undo()
        val placeStatus = board.placePiece(currentPiece, previousX, previousY, previousZ)
        when (placeStatus) {
            TetracubeBoard.PlaceStatus.OVERLAP_WITH_EXISTING, TetracubeBoard.PlaceStatus.OUT_OF_BOUNDS -> {
                // Not a valid move, revert to previous state
                // TODO: Check if it's possible to check for this case, instead of doing & then reverting
                currentPiece.rotateCounterClockwise()
                board.undo()
                board.placePiece(currentPiece, previousX, previousY, previousZ)
            }
            else -> {
                // It's valid, peace out.
            }
        }
        gameStateListener?.onGridUpdated(board.grid, board.height, board.width, board.breadth)
    }

    fun rotateCounterClockwise() {
        currentPiece.rotateCounterClockwise()
        board.undo()
        val placeStatus = board.placePiece(currentPiece, previousX, previousY, previousZ)
        when (placeStatus) {
            TetracubeBoard.PlaceStatus.OVERLAP_WITH_EXISTING, TetracubeBoard.PlaceStatus.OUT_OF_BOUNDS -> {
                // Not a valid move, revert to previous state
                // TODO: Check if it's possible to check for this case, instead of doing & then reverting
                currentPiece.rotateClockwise()
                board.undo()
                board.placePiece(currentPiece, previousX, previousY, previousZ)
            }
            else -> {
                // It's valid, peace out.
            }
        }
        gameStateListener?.onGridUpdated(board.grid, board.height, board.width, board.breadth)
    }

    private fun prepareNewPiece() {
        currentPiece = nextPiece
        nextPiece = PieceUtil.getRandomPiece()
        gameStateListener?.onPieceChanged(currentPiece, nextPiece)
        previousX = board.width / 2
        previousZ = board.breadth / 2
        previousY = board.height - (currentPiece.height - 1)
        board.commit()
    }

}