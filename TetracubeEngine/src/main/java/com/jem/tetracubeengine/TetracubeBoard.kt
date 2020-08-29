package com.jem.tetracubeengine

import com.jem.tetracubeengine.pieces.Piece

class TetracubeBoard(
    val width: Int = DEFAULT_WIDTH,
    val height: Int = DEFAULT_HEIGHT,
    val breadth: Int = DEFAULT_BREADTH
) {

    /**
     * The number of blocks filled in each width-row given (y, z)
     */
    var widths: Array<IntArray> = Array(height) { IntArray(breadth) }

    /**
     * The index of open spot above topmost block in each column (x, y)
     */
    var heights: Array<IntArray> = Array(width) { IntArray(breadth) }

    /**
     * The number of blocks filled in each breadth-row given (x, y)
     */
    var breadths: Array<IntArray> = Array(height) { IntArray(width) }

    /**
     * The maximum height of the currently placed blocks (largest value from heights array).
     * Used to clear the rows in an optimal manner
     */
    var maxHeight: Int = 0

    /**
     * The grid represents the possible block spots and determines which spots are filled/empty.
     * If spot at (x,y,z) is true, then it means that a block is at (x,y,z)
     */
    val grid: Array<Array<BooleanArray>> = Array(height) { Array(breadth) { BooleanArray(width) } }

    /**
     * Enums depicting the possible status values that
     * can be gotten when attempting to place a piece in the board.
     */
    enum class PlaceStatus {
        /**
         * Successfully placed piece without issues
         */
        SUCCESS,

        /**
         * Placing piece caused layer to be filled (Seriously? Does this even happen!?)
         */
        LAYER_FILLED,

        /**
         * One or more blocks of the piece are outside the bounds of the board grid
         */
        OUT_OF_BOUNDS,

        /**
         * One or more blocks of the piece overlap with blocks already in the board grid
         * Basically, it's game over. :)
         */
        OVERLAP_WITH_EXISTING
    }

    companion object {
        val DEFAULT_WIDTH = 12
        val DEFAULT_HEIGHT = 24

        // TODO: Change value to non-zero and verify if impl works for Tetracube properly
        val DEFAULT_BREADTH = 0 /*12*/
    }
}