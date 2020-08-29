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

    fun placePiece(piece: Piece, x: Int, y: Int, z: Int): PlaceStatus {
        // TODO: Add breadth based handling after Pieces have been converted to 3D
        var wasAnyLayerFilled = false
        // Check if block is within the bounds of the grid or if it overlaps with existing blocks
        for (block in piece.body) {
            if (block.x < 0 || block.x > width - 1 ||
                block.y < 0 || block.y > height - 1
//                block.z < 0 || block.z > width - 1
            ) {
                return PlaceStatus.OUT_OF_BOUNDS
            } else if (grid[block.y][block.x][0]) {
                return PlaceStatus.OVERLAP_WITH_EXISTING
            }
            grid[block.y][block.x][0] = true
            if (isLayerFull(y)) {
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

    private fun isLayerFull(layer: Int): Boolean {
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
        val DEFAULT_WIDTH = 12
        val DEFAULT_HEIGHT = 24

        // TODO: Change value to non-zero and verify if impl works for Tetracube properly
        val DEFAULT_BREADTH = 0 /*12*/
    }
}