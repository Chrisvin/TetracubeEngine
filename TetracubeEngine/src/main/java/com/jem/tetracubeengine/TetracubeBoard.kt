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
     * NOTE: Grid array is in the format [y][x][z] to optimize clear row fn
     */
    val grid: Array<Array<BooleanArray>> = Array(height) { Array(width) { BooleanArray(breadth) } }

    /**
     * Denotes whether the board is in commited state or not.
     * (i.e) Whether current state & backup state are the same or not.
     */
    var isCommitted: Boolean = false

    // Backup variables

    /**
     * Backup of the widths for fast undo
     */
    var backupWidths: Array<IntArray> = Array(height) { IntArray(breadth) }

    /**
     * Backup of the heights for fast undo
     */
    var backupHeights: Array<IntArray> = Array(width) { IntArray(breadth) }

    /**
     * Backup of the breadths for fast undo
     */
    var backupBreadths: Array<IntArray> = Array(height) { IntArray(width) }

    /**
     * Backup of the max height for fast undo
     */
    var backupMaxHeight: Int = maxHeight

    /**
     * Backup of the grid for fast undo
     */
    val backupGrid: Array<Array<BooleanArray>> = grid

    /**
     * Place the piece on the board in the specified (x, y, z) position
     */
    fun placePiece(
        piece: Piece,
        x: Int,
        y: Int,
        z: Int,
        checkLayerFilled: Boolean = false
    ): PlaceStatus {
        // TODO: Add breadth based handling after Pieces have been converted to 3D
        isCommitted = false
        var wasAnyLayerFilled = false
        // Check if block is within the bounds of the grid or if it overlaps with existing blocks
        for (block in piece.body) {
            val blockX = block.x + x
            val blockY = block.y + y
            val blockZ = 0/*block.z*/ + z
            if (blockX < 0 || blockX > width - 1 ||
                blockY < 0 || blockY > height - 1 ||
                blockZ < 0 || blockZ > breadth - 1
            ) {
                return PlaceStatus.OUT_OF_BOUNDS
            } else if (grid[blockY][blockX][blockZ]) {
                return PlaceStatus.OVERLAP_WITH_EXISTING
            }
            // Fill the spot with the block & update corresponding variables
            grid[blockY][blockX][blockZ] = true
            widths[blockY][blockZ]++
            breadths[blockY][blockX]++
            if (heights[blockX][blockZ] < blockY + 1) {
                heights[blockX][blockZ] = blockY + 1
                if (maxHeight < heights[blockX][blockZ]) {
                    maxHeight = heights[blockX][blockZ]
                }
            }
            // Check if block causes the layer to fill up
            if (checkLayerFilled && isLayerFull(blockY, blockX, blockZ)) {
                wasAnyLayerFilled = true
            }
        }
        return if (wasAnyLayerFilled) {
            PlaceStatus.LAYER_FILLED
        } else {
            PlaceStatus.SUCCESS
        }
    }

    /**
     * Drop piece from given (x, z) position
     * @return y value where the origin (0,0) of a piece will come to rest
     */
    fun dropHeight(piece: Piece, x: Int, z: Int): Int {
        // TODO: Add breadth based handling after Pieces have been converted to 3D
        // Initially assume it'll fall till the ground
        var highestCollisionPoint = 0
        for (w in 0 until piece.width) {
            if (highestCollisionPoint < heights[x + w][z] - piece.skirt[w]) {
                highestCollisionPoint = heights[x + w][z] - piece.skirt[w]
            }
        }
        return highestCollisionPoint
    }

    /**
     * Clears the layers that are filled
     * @return ArrayList containing layer indices that were cleared
     */
    fun clearLayers(): ArrayList<Int> {
        val layersCleared = ArrayList<Int>()
        // Layers to drop down (fromLayer -> toLayer) since some layers below were cleared
        val layersToDrop: ArrayList<Pair<Int, Int>> = ArrayList()
        for (h in 0 until height) {
            if (isLayerFull(h)) {
                layersCleared.add(h)
            } else if (layersCleared.size > 0) {
                layersToDrop.add(Pair(h, h - layersCleared.size))
            }
        }
        maxHeight -= layersCleared.size
        // Check if layers were cleared, and move blocks down accordingly
        if (layersCleared.size > 0) {
            isCommitted = false
            for (layer in layersToDrop) {
                System.arraycopy(
                    grid[layer.first], 0,
                    grid[layer.second], 0,
                    grid[layer.first].size
                )
            }
            for (layer in 1..layersCleared.size) {
                for (w in 0 until width) {
                    for (b in 0 until breadth) {
                        grid[maxHeight + layer][w][b] = false
                        if (layer == 1) {
                            heights[w][b] -= layersCleared.size
                        }
                    }
                }
            }
        }
        return layersCleared
    }

    /**
     * Commits the state of the board for future undo
     */
    fun commit() {
        if (isCommitted) {
            // Already in committed state
            return
        }
        backupMaxHeight = maxHeight
        copyState(
            widths, heights,
            breadths, grid,
            backupWidths, backupHeights,
            backupBreadths, backupGrid
        )
        // Updated to new commit state
        isCommitted = true
    }

    /**
     * Undo to the last commit state
     */
    fun undo() {
        if (isCommitted) {
            // Already in committed state
            return
        }
        maxHeight = backupMaxHeight
        copyState(
            backupWidths, backupHeights,
            backupBreadths, backupGrid,
            widths, heights,
            breadths, grid
        )
        // Reverted to old commit state
        isCommitted = true
    }

    /**
     * Helper function to copy array values
     */
    private fun copyState(
        fromWidths: Array<IntArray>, fromHeights: Array<IntArray>,
        fromBreadths: Array<IntArray>, fromGrid: Array<Array<BooleanArray>>,
        toWidths: Array<IntArray>, toHeights: Array<IntArray>,
        toBreadths: Array<IntArray>, toGrid: Array<Array<BooleanArray>>
    ) {
        System.arraycopy(fromWidths, 0, toWidths, 0, fromWidths.size)
        System.arraycopy(fromHeights, 0, toHeights, 0, fromHeights.size)
        System.arraycopy(fromBreadths, 0, toBreadths, 0, fromBreadths.size)
        for (h in 0 until height) {
            for (w in 0 until width) {
                System.arraycopy(fromGrid[h][w], 0, toGrid[h][w], 0, breadth)
            }
        }
    }

    /**
     * Checks if given layer is filled.
     * Has minor optimization if (x, z) values are provided
     */
    private fun isLayerFull(layer: Int, x: Int = 0, z: Int = 0): Boolean {
        if (widths[layer][z] != width || breadths[layer][x] != breadth) {
            // Minor optimization, First check if the row that block was added to is filled.
            // During a normal game, it's much more likely that a block will not fill up a layer.
            return false
        }
        for (w in widths[layer]) {
            if (w != width) {
                return false
            }
        }
        for (b in breadths[layer]) {
            if (b != breadth) {
                return false
            }
        }
        return true
    }

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
         * Placing piece caused layer to be filled
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
        const val DEFAULT_WIDTH = 12
        const val DEFAULT_HEIGHT = 24

        // TODO: Change value to non-zero and verify if impl works for Tetracube properly
        const val DEFAULT_BREADTH = 1 /*12*/
    }
}